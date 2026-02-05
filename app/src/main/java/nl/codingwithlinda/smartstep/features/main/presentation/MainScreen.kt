package nl.codingwithlinda.smartstep.features.main.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.navigation.NavigationController
import nl.codingwithlinda.smartstep.navigation.UserSettingsRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()


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
                    dailyGoal = 2000
                )
            }
        }


    }
}