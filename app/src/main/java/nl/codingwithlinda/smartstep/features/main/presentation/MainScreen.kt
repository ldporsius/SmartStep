package nl.codingwithlinda.smartstep.features.main.presentation

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.core.domain.util.ObserveAsEvents
import nl.codingwithlinda.smartstep.core.presentation.util.necessaryPermissionsOnly
import nl.codingwithlinda.smartstep.core.presentation.util.permissionsPerBuild
import nl.codingwithlinda.smartstep.features.daily_step_goal.DailyStepGoalViewModel
import nl.codingwithlinda.smartstep.features.main.domain.concrete_states.BackgroundRunningAllowed
import nl.codingwithlinda.smartstep.features.main.navigation.controller.MainNavAction
import nl.codingwithlinda.smartstep.features.main.navigation.drawer.MainNavDrawer
import nl.codingwithlinda.smartstep.features.main.navigation.drawer.navDrawerItems
import nl.codingwithlinda.smartstep.features.main.navigation.nav_drawer_events.controllers.MainNavItemHandler
import nl.codingwithlinda.smartstep.features.main.presentation.battery_optimization.isIgnoringBatteryOptimizations
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_card.DailyStepCountViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.main_screen_content_provider.MainScreenContent
import nl.codingwithlinda.smartstep.features.main.presentation.main_screen_content_provider.MainScreenDecorator
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.PermissionDecorator
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.PermissionUiState
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.PermissionsViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.toPermissionUiState
import nl.codingwithlinda.smartstep.features.statistics.presentation.StatisticsViewModel
import nl.codingwithlinda.smartstep.features.step_tracker.presentation.StepTrackerViewModel
import nl.codingwithlinda.smartstep.features.steps_override_user.navigation.UserOverrideStepsNavActionDecorator
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

            activity?.let { ac ->
                val isIgnoringBatteryOptimizations = isIgnoringBatteryOptimizations(context)

                println("isIgnoringBatteryOptimizations: $isIgnoringBatteryOptimizations")

                when (isIgnoringBatteryOptimizations) {
                    true ->{
                        permissionsViewModel.setTrackingState(BackgroundRunningAllowed(ac))
                    }
                    false -> {
                        navItemHandler.handleAction(MainNavAction.BACKGROUND_ACCESS_RECOMMENDED)
                    }
                }
            }
        }

        //handle remaining permissions
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

    ObserveAsEvents(permissionsViewModel.startTrackingState) {
        it.startTracking()
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

                MainScreenContent(
                    dailyStepGoalViewModel = dailyStepGoalViewModel,
                    dailyStepCountViewModel = dailyStepCountViewModel,
                    statisticsViewModel = statisticsViewModel,
                    stepTrackerViewModel = stepTrackerViewModel,
                    weeklyAverageViewModel = weeklyAverageViewModel
                )

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

    //put outside the main nav drawer because swipe action in lazycolumn interferes with opening drawer
    Box(
        modifier = Modifier.systemBarsPadding()
    ) {
        MainScreenDecorator(
            mainNavAction = actions,
            navItemHandler = navItemHandler,
            parent = this
        )

        UserOverrideStepsNavActionDecorator(
           currentStep = dailyStepCountViewModel.todaysStep.collectAsStateWithLifecycle().value ?: return@Box
        )
    }

    //debug
   /* ObserveAsEvents(stepTrackerViewModel.state) {
        Toast.makeText(context, it.name, Toast.LENGTH_SHORT).show()
    }*/



}