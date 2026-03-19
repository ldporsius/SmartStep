package nl.codingwithlinda.smartstep.features.main.presentation

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.core.data.step_tracker_finite_state.SmartStepStateControllerImpl
import nl.codingwithlinda.smartstep.core.domain.step_tracker_finite_state.SmartStepStateController
import nl.codingwithlinda.smartstep.core.presentation.util.ObserveAsEvents
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_count.DailyStepCountViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_goal.DailyStepGoalViewModel
import nl.codingwithlinda.smartstep.features.main.navigation.controller.MainNavAction
import nl.codingwithlinda.smartstep.features.main.navigation.drawer.MainNavDrawer
import nl.codingwithlinda.smartstep.features.main.navigation.drawer.navDrawerItems
import nl.codingwithlinda.smartstep.features.main.navigation.nav_drawer_events.controllers.MainNavActionControllerImpl
import nl.codingwithlinda.smartstep.features.main.presentation.main_screen_content_provider.MainNavItemHandler
import nl.codingwithlinda.smartstep.features.main.presentation.main_screen_content_provider.MainScreenContent
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.PermissionDecorator
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.PermissionsViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.steps_override_user.navigation.UserOverrideStepsNavActionDecorator
import nl.codingwithlinda.smartstep.features.main.statistics.presentation.StatisticsViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.walk_duration.presentation.WalkDurationViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.weekly_average.presentation.WeeklyAverageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    dailyStepGoalViewModel: DailyStepGoalViewModel,
    dailyStepCountViewModel: DailyStepCountViewModel,
    statisticsViewModel: StatisticsViewModel,
    walkDurationViewModel: WalkDurationViewModel,
    weeklyAverageViewModel: WeeklyAverageViewModel,
    editStepsViewModel: nl.codingwithlinda.smartstep.features.main.presentation.steps_override_user.edit.presentation.EditStepsViewModel,
    resetStepsViewModel: nl.codingwithlinda.smartstep.features.main.presentation.steps_override_user.reset.presentation.ResetStepsViewModel,
    smartStepStateController: SmartStepStateController,
    aiMessageComponent: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val permissionsViewModel = viewModel<PermissionsViewModel>()
    val navItemHandler = MainNavActionControllerImpl
    val actions = navItemHandler.actions.collectAsStateWithLifecycle(MainNavAction.NA).value

    ObserveAsEvents(walkDurationViewModel.state) {
        Toast.makeText(context, "${it.name}", Toast.LENGTH_SHORT).show()
    }

    ObserveAsEvents(smartStepStateController.state) {
        it.startTracking()
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect (lifecycleOwner){
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
            withContext(Dispatchers.Main) {
                smartStepStateController.onResult()
            }
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

                MainScreenContent(
                    dailyStepGoalViewModel = dailyStepGoalViewModel,
                    dailyStepCountViewModel = dailyStepCountViewModel,
                    statisticsViewModel = statisticsViewModel,
                    walkDurationViewModel = walkDurationViewModel,
                    weeklyAverageViewModel = weeklyAverageViewModel,
                    aiMessageComponent = aiMessageComponent
                )
            }

            PermissionDecorator(
                smartStepStateController = smartStepStateController,
                permissionsViewModel = permissionsViewModel,
                navItemHandler = navItemHandler,
                requestPermission = {
                    smartStepStateController.onResult()
                }
            )
        }
    }

    //put outside the main nav drawer because swipe action in lazycolumn interferes with opening drawer
    Box(
        modifier = Modifier.systemBarsPadding()
    ) {
        MainNavItemHandler(
            dailyStepGoalViewModel = dailyStepGoalViewModel,
            mainNavAction = actions,
            navItemHandler = navItemHandler,
            smartStepStateController = smartStepStateController,
        )
        UserOverrideStepsNavActionDecorator(
            editStepsViewModel = editStepsViewModel,
            resetStepsViewModel = resetStepsViewModel

        )
    }

}