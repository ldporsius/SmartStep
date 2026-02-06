package nl.codingwithlinda.smartstep.features.main.presentation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.features.main.navigation.FixStepProblemNavItem
import nl.codingwithlinda.smartstep.features.main.navigation.MainNavAction
import nl.codingwithlinda.smartstep.features.main.navigation.MainNavDrawer
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.BackgroundAccessRecommendedDialog
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.PermissionUiState
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.PermissionsViewModel
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.isIgnoringBatteryOptimizations
import nl.codingwithlinda.smartstep.features.main.presentation.state.MainNavItemHandler
import nl.codingwithlinda.smartstep.navigation.NavigationController
import nl.codingwithlinda.smartstep.navigation.UserSettingsRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalActivity.current

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
            val isIgnoringBatteryOptimizations = context?.let { isIgnoringBatteryOptimizations(context)} ?: false
            println("isIgnoringBatteryOptimizations: $isIgnoringBatteryOptimizations")
            if (!isIgnoringBatteryOptimizations) {
                permissionsViewModel.setPermissionState(PermissionUiState.BACKGROUND_ACCESS_RECOMMENDED)
            }


        } else {
            //find out if permanently declined
            context?.let {
                val shouldShowRationaleBodySensors =
                    shouldShowRequestPermissionRationale(context, Manifest.permission.BODY_SENSORS)

                if (shouldShowRationaleBodySensors) {
                    permissionsViewModel.setPermissionState(PermissionUiState.DENIED)
                } else {
                    permissionsViewModel.setPermissionState(PermissionUiState.DENIED_PERMANENTLY)
                }
            }
        }
    }


    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_START) {
                permissionLauncher.launch(android.Manifest.permission.ACTIVITY_RECOGNITION)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val isIgnoringBatteryOptimizations = context?.let { isIgnoringBatteryOptimizations(context)} ?: false

    val navItemHandler = MainNavItemHandler
    MainNavDrawer(
        drawerState = drawerState,
        scope = scope,
        mainNavActionController = navItemHandler,
        items = listOf(
            FixStepProblemNavItem(
                title = "Fix step problem",
                shouldShowInDrawer = {
                    isIgnoringBatteryOptimizations
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
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(it),
                contentAlignment = androidx.compose.ui.Alignment.Center) {


                DailyStepCard(
                    stepsTaken = 1000,
                    dailyGoal = 2000,
                    modifier = Modifier
                        .semantics {
                            contentDescription = "Daily Step Card"
                        }
                )
            }
        }

        val shouldShowBottomSheet = permissionsViewModel.shouldShowBottomSheet.collectAsStateWithLifecycle().value
        if (shouldShowBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    // permissionsViewModel.setPermissionGranted(PermissionUiState.NA)
                }
            ) {
                permissionsViewModel.BottomSheetContent(
                    batteryOptimizeLauncher = batteryOptimizeLauncher,
                    permissionLauncher = permissionLauncher)
            }

        }
        val actions = navItemHandler.actions.collectAsStateWithLifecycle(null).value

        var shouldShowBottomSheet2 by remember(actions) { mutableStateOf(actions != null) }


        if (shouldShowBottomSheet2) {
            ModalBottomSheet(
                onDismissRequest = {
                    shouldShowBottomSheet2 = false
                }
            ) {

                    when (actions) {
                        MainNavAction.NA -> {
                            Text("NA")
                        }

                        MainNavAction.BACKGROUND_ACCESS_RECOMMENDED -> {
                            BackgroundAccessRecommendedDialog(
                                onClick = {
                                    val intent = Intent(
                                        Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                                    ).apply {
                                        data = android.net.Uri.parse("package:${context?.packageName}")

                                    }
                                    val intent1 = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS).apply {
                                        data = Uri.parse("package:${context?.packageName}")
                                    }
                                    batteryOptimizeLauncher.launch(intent1)
                                },
                                modifier = Modifier
                                    .padding(48.dp)
                            )
                        }

                        MainNavAction.DAILY_STEP_GOAL -> {
                            Text("Daily step goal")
                        }

                        MainNavAction.EXIT -> {
                            Text("Exit")
                        }
                        else -> Unit
                    }
                }
            }
        }

}