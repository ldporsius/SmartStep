package nl.codingwithlinda.smartstep.features.main.navigation.drawer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.design_system.ui.theme.primary
import nl.codingwithlinda.smartstep.features.main.navigation.controller.MainNavAction
import nl.codingwithlinda.smartstep.features.main.navigation.controller.MainNavActionController

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
                                it.onAction()
                                scope.launch {
                                    drawerState.close()
                                }
                            }
                        )

                        HorizontalDivider()
                    }
                }

                NavigationDrawerItem(
                    label = {
                        Text("Exit",
                        color = primary) },
                    selected = false,
                    onClick = {
                        mainNavActionController.handleAction(MainNavAction.EXIT)
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