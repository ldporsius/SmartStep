package nl.codingwithlinda.smartstep.features.main.navigation

import android.R.attr.label
import android.R.attr.onClick
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.navigation.NavigationController
import nl.codingwithlinda.smartstep.navigation.UserSettingsRoute

@Composable
fun MainNavDrawer(
    drawerState: DrawerState,
    scope: CoroutineScope,
    items: List<NavDrawerItem> = emptyList(),
    mainNavActionController: MainNavActionController,
    content: @Composable () -> Unit
) {



    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerState = drawerState
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                //only if there is an issue
                items.onEach {
                    if (it.visible()) {
                        NavigationDrawerItem(
                            label = { Text(it.title) },
                            selected = false,
                            onClick = {
                                mainNavActionController.handleAction(it.onAction())
                                scope.launch {
                                    drawerState.close()
                                }
                            }
                        )
                    }
                }

                NavigationDrawerItem(
                    label = { Text("Step goal") },
                    selected = false,
                    onClick = {
                        //open a bottom sheet in MainScreen
                        mainNavActionController.handleAction(MainNavAction.DAILY_STEP_GOAL)
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
                NavigationDrawerItem(
                        label = { Text("Personal settings") },
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
    ){
        content()
    }
}