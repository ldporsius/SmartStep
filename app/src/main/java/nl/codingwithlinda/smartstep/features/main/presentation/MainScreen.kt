package nl.codingwithlinda.smartstep.features.main.presentation

import android.Manifest
import androidx.activity.compose.LocalActivity
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.BodySensorsPermissionDeclinedDialogProvider
import nl.codingwithlinda.smartstep.features.main.presentation.permissions.BodySensorsPermissionRationaleDialogProvider
import nl.codingwithlinda.smartstep.navigation.NavigationController
import nl.codingwithlinda.smartstep.navigation.UserSettingsRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalActivity.current

    var timesPermissionAsked by rememberSaveable { mutableStateOf(0) }
    var shouldShowRationaleBodySensors by remember { mutableStateOf(false) }
    var permissionBodySensorsDeclined by remember { mutableStateOf(false) }

    val showPermissionDialog = shouldShowRationaleBodySensors || permissionBodySensorsDeclined


    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {granted->
        println("---- timesPermissionAsked: $timesPermissionAsked, granted: $granted")
        if (granted){
            //continue asking for background activity permissions
            shouldShowRationaleBodySensors = false
            permissionBodySensorsDeclined = false
        }
        else {
            //find out if permanently declined
            context?.let {
                shouldShowRationaleBodySensors =
                    shouldShowRequestPermissionRationale(context, Manifest.permission.BODY_SENSORS) || timesPermissionAsked == 0
                permissionBodySensorsDeclined = !shouldShowRationaleBodySensors && timesPermissionAsked > 0

                println("---- shouldShowRationaleBodySensors: $shouldShowRationaleBodySensors")
                println("---- permissionBodySensorsDeclined: $permissionBodySensorsDeclined")
            }
        }
        timesPermissionAsked++
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_START) {
                permissionLauncher.launch(android.Manifest.permission.BODY_SENSORS)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerState = drawerState
            ) {
                NavigationDrawerItem(
                    label = { Text("Settings") },
                    selected = false,
                    onClick = {
                        NavigationController.navigateTo(UserSettingsRoute)
                        scope.launch {
                            drawerState.close()
                        }

                    }
                )
            }
        }
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

            if (showPermissionDialog) {
                ModalBottomSheet(
                    onDismissRequest = {
                        shouldShowRationaleBodySensors = false
                        permissionBodySensorsDeclined = false
                    }
                ) {
                    if (permissionBodySensorsDeclined){
                        BodySensorsPermissionDeclinedDialogProvider().Description()
                    }
                    else if (shouldShowRationaleBodySensors){
                        BodySensorsPermissionRationaleDialogProvider(
                            onClick = {
                                permissionLauncher.launch(Manifest.permission.BODY_SENSORS)
                            }
                        ).Description()
                    }
                }
            }
        }
    }
}