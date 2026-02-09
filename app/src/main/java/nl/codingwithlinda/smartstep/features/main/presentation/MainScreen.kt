package nl.codingwithlinda.smartstep.features.main.presentation

import android.Manifest
import android.R.attr.onClick
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.core.domain.util.MessageFromAnywhere
import nl.codingwithlinda.smartstep.core.domain.util.ObserveAsEvents
import nl.codingwithlinda.smartstep.features.main.navigation.FixStepProblemNavItem
import nl.codingwithlinda.smartstep.features.main.navigation.MainNavAction
import nl.codingwithlinda.smartstep.features.main.navigation.MainNavDrawer
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_goal.DailyStepGoalComponent
import nl.codingwithlinda.smartstep.features.main.presentation.daily_step_goal.DailyStepGoalViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.exit.ExitDialog
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.PermissionUiState
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.PermissionsViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.isIgnoringBatteryOptimizations
import nl.codingwithlinda.smartstep.features.main.presentation.state.MainNavItemHandler
import nl.codingwithlinda.smartstep.features.main.presentation.state.MainScreenDecorator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
     dailyStepGoalViewModel: DailyStepGoalViewModel
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val activity = LocalActivity.current
    val context = LocalContext.current

    val permissionsViewModel = viewModel<PermissionsViewModel>()

    val batteryOptimizeLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            println("--- BATTERY OPTIMIZE LAUNCHER RETURNED WITH RESULT: ${it.resultCode}, ${it.data}")
            when(it.resultCode){
                0 -> {
                    //not granted
                }
                else -> {
                    //granted
                }
            }
            permissionsViewModel.setPermissionState(PermissionUiState.NA)
        }

    val permissionLauncher: ManagedActivityResultLauncher<String, Boolean> = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->

        if (granted) {
            //continue asking for background activity permissions
            val isIgnoringBatteryOptimizations = activity?.let { isIgnoringBatteryOptimizations(context)} ?: false
            println("isIgnoringBatteryOptimizations: $isIgnoringBatteryOptimizations")
            if (!isIgnoringBatteryOptimizations) {
                permissionsViewModel.setPermissionState(PermissionUiState.BACKGROUND_ACCESS_RECOMMENDED)
            }


        } else {
            //find out if permanently declined
            activity?.let {ac->
                val shouldShowRationaleBodySensors =
                    shouldShowRequestPermissionRationale(ac, Manifest.permission.BODY_SENSORS)

                if (shouldShowRationaleBodySensors) {
                    permissionsViewModel.setPermissionState(PermissionUiState.DENIED)
                } else {
                    permissionsViewModel.setPermissionState(PermissionUiState.DENIED_PERMANENTLY)
                }
            }
        }
    }


    var shouldShowFixBatteryInDrawer by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
                val isIgnoringBatteryOptimizations = activity?.let {
                    isIgnoringBatteryOptimizations(it)} ?: false
                shouldShowFixBatteryInDrawer = !isIgnoringBatteryOptimizations

            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    val navItemHandler = MainNavItemHandler
    val actions = navItemHandler.actions.collectAsStateWithLifecycle(MainNavAction.NA).value

    MainNavDrawer(
        drawerState = drawerState,
        scope = scope,
        mainNavActionController = navItemHandler,
        items = listOf(
            FixStepProblemNavItem(
                title = "Fix step problem",
                shouldShowInDrawer = {
                    shouldShowFixBatteryInDrawer
                }
            )
        )
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

        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                contentAlignment = Alignment.Center
            ) {
                DailyStepCard(
                    stepsTaken = dailyStepGoalViewModel.stepsTaken.collectAsStateWithLifecycle().value,
                    dailyGoal = dailyStepGoalViewModel.goal.collectAsStateWithLifecycle().value,
                    modifier = Modifier
                        .semantics {
                            contentDescription = "Daily Step Card"
                        }
                )

                MainScreenDecorator(
                    mainNavAction = actions,
                    navItemHandler = navItemHandler,
                    permissionsViewModel = permissionsViewModel,
                    dailyStepGoalViewModel = dailyStepGoalViewModel,
                    parent = this
                )
            }


            val shouldShowBottomSheet =
                permissionsViewModel.shouldShowBottomSheet.collectAsStateWithLifecycle().value
            if (shouldShowBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        permissionsViewModel.setPermissionState(PermissionUiState.NA)
                        navItemHandler.handleAction(MainNavAction.NA)
                    }
                ) {
                    permissionsViewModel.BottomSheetContent(
                        batteryOptimizeLauncher = batteryOptimizeLauncher,
                        permissionLauncher = permissionLauncher
                    )
                }

            }

        }
    }
}