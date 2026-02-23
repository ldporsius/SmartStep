package nl.codingwithlinda.smartstep.features.main.presentation

import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.core.data.step_tracker.StepTrackerService
import nl.codingwithlinda.smartstep.core.domain.util.ObserveAsEvents
import nl.codingwithlinda.smartstep.core.presentation.util.necessaryPermissionsOnly
import nl.codingwithlinda.smartstep.core.presentation.util.permissionsPerBuild
import nl.codingwithlinda.smartstep.features.main.navigation.controller.MainNavAction
import nl.codingwithlinda.smartstep.features.main.navigation.drawer.MainNavDrawer
import nl.codingwithlinda.smartstep.features.main.navigation.drawer.navDrawerItems
import nl.codingwithlinda.smartstep.features.main.presentation.battery_optimization.isIgnoringBatteryOptimizations
import nl.codingwithlinda.smartstep.features.daily_step_goal.DailyStepGoalViewModel
import nl.codingwithlinda.smartstep.features.main.navigation.controller.StepNavAction
import nl.codingwithlinda.smartstep.features.main.navigation.nav_drawer_events.controllers.MainNavItemHandler
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_card.DailyStepCard
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_card.DailyStepCountViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.PermissionDecorator
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.PermissionUiState
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.PermissionsViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.canStartStepTrackerService
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.toPermissionUiState
import nl.codingwithlinda.smartstep.features.main.presentation.main_screen_content_provider.MainScreenDecorator
import nl.codingwithlinda.smartstep.features.step_tracker.presentation.StepTrackerViewModel
import nl.codingwithlinda.smartstep.features.statistics.presentation.StatisticsViewModel
import nl.codingwithlinda.smartstep.features.steps_override_user.navigation.StepNavActionHandler
import nl.codingwithlinda.smartstep.features.steps_override_user.navigation.StepsNavActionDecorator
import nl.codingwithlinda.smartstep.features.weekly_average.presentation.WeeklyAverageScreen
import nl.codingwithlinda.smartstep.features.weekly_average.presentation.WeeklyAverageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    dailyStepGoalViewModel: DailyStepGoalViewModel,
    dailyStepCountViewModel: DailyStepCountViewModel,
    statisticsViewModel: StatisticsViewModel,
    stepTrackerViewModel: StepTrackerViewModel,
    weeklyAverageViewModel: WeeklyAverageViewModel
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val activity = LocalActivity.current
    val context = LocalContext.current

    val permissionsViewModel = viewModel<PermissionsViewModel>()
    val navItemHandler = MainNavItemHandler
    val actions = navItemHandler.actions.collectAsStateWithLifecycle(MainNavAction.NA).value

    val permissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultMap ->
        val allGranted = resultMap.all {
            it.value
        }
        if(allGranted){
            permissionsViewModel.setPermissionState(PermissionUiState.NA)
            val isIgnoringBatteryOptimizations = activity?.let { isIgnoringBatteryOptimizations(context)} ?: false
            println("isIgnoringBatteryOptimizations: $isIgnoringBatteryOptimizations")
            if (!isIgnoringBatteryOptimizations) {
                navItemHandler.handleAction(MainNavAction.BACKGROUND_ACCESS_RECOMMENDED)
            }
        }

        activity?.let { ac->
            if(ac.canStartStepTrackerService()){
                val trackerIntent = Intent(ac, StepTrackerService::class.java).apply {
                    action = StepTrackerService.ACTION_START
                }
                ac.startService(trackerIntent)
            }
        }
        resultMap.filter {
            it.value == false
        }.toList().firstOrNull()?.let {
            val perm = it.first
            val uiState = activity?.toPermissionUiState(perm) ?: PermissionUiState.NA

            permissionsViewModel.setPermissionState(uiState)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                permissionsPerBuild(Build.VERSION.SDK_INT).let {requiredPerms ->
                    permissionsLauncher.launch(
                        requiredPerms.toTypedArray()
                    )
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }



    MainNavDrawer(
        drawerState = drawerState,
        scope = scope,
        mainNavActionController = navItemHandler,
        items = navDrawerItems()
    ) {

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("SmartStep") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.menu_burger_square_1),
                                contentDescription = "Menu"
                            )
                        }

                    },
                    colors = TopAppBarDefaults.topAppBarColors().copy(
                        containerColor = Color.Transparent
                    )
                )
            }

        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DailyStepCard(
                        stepsTaken = dailyStepCountViewModel.stepsToday.collectAsStateWithLifecycle().value,
                        dailyGoal = dailyStepGoalViewModel.goal.collectAsStateWithLifecycle().value,
                        statisticsUi = statisticsViewModel.statistics.collectAsStateWithLifecycle().value,
                        stepTrackerState = stepTrackerViewModel.state.collectAsStateWithLifecycle().value,
                        actionEdit = {
                            StepNavActionHandler.handleAction(StepNavAction.EDIT_STEPS)
                        },
                        actionPause = {
                            stepTrackerViewModel.pause()
                        },
                        actionPlay = {
                            stepTrackerViewModel.start()
                        },
                        modifier = Modifier
                            .semantics {
                                contentDescription = "Daily Step Card"
                            }
                            .padding(16.dp)
                    )

                    WeeklyAverageScreen(
                        days = weeklyAverageViewModel.lastSevenStepCounts.collectAsStateWithLifecycle().value,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    )
                }


                ///debug
                Box(
                    modifier = Modifier.align(Alignment.BottomCenter)
                ){
                    val count = stepTrackerViewModel.counter.collectAsStateWithLifecycle().value
                    Text("$count")
                }
            }

            PermissionDecorator(
                permissionsViewModel = permissionsViewModel,
                navItemHandler = navItemHandler,
                requestPermission = {
                    necessaryPermissionsOnly().let {
                        permissionsLauncher.launch(it.toTypedArray())
                    }
                }
            )
        }
    }

    Box(
        modifier = Modifier.systemBarsPadding()

    ) {
        MainScreenDecorator(
            mainNavAction = actions,
            navItemHandler = navItemHandler,
            parent = this
        )

        StepsNavActionDecorator(
           currentStep = dailyStepCountViewModel.todaysStep.collectAsStateWithLifecycle().value ?: return@Box
        )
    }

    //debug
    ObserveAsEvents(stepTrackerViewModel.state) {
        Toast.makeText(context, it.name, Toast.LENGTH_SHORT).show()
    }



}