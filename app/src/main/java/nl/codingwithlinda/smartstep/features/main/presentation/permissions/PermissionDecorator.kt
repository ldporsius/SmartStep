package nl.codingwithlinda.smartstep.features.main.presentation.permissions

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.codingwithlinda.smartstep.features.main.navigation.MainNavAction
import nl.codingwithlinda.smartstep.features.main.presentation.state.MainBottomSheetController
import nl.codingwithlinda.smartstep.features.main.presentation.state.MainNavItemHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDecorator(
    permissionsViewModel: PermissionsViewModel,
    navItemHandler: MainBottomSheetController = MainNavItemHandler,
    requestPermission: () -> Unit
    ) {
    val density = LocalDensity.current.density
    val isLargeScreen = LocalWindowInfo.current.containerSize.width > 840 * density
    val shouldShowUserInteraction =
        permissionsViewModel.shouldShowUserInteraction.collectAsStateWithLifecycle().value
    if (shouldShowUserInteraction) {
        if (isLargeScreen){
            Dialog(
                onDismissRequest = {
                    permissionsViewModel.setPermissionState(PermissionUiState.NA)
                    navItemHandler.handleAction(MainNavAction.NA)
                }
            ) {
                Surface {
                    permissionsViewModel.BottomSheetContent(
                        requestActivityRecognition = requestPermission
                    )
                }
            }
        }
        else {
            ModalBottomSheet(
                onDismissRequest = {
                    permissionsViewModel.setPermissionState(PermissionUiState.NA)
                    navItemHandler.handleAction(MainNavAction.NA)
                }
            ) {
                permissionsViewModel.BottomSheetContent(
                    requestActivityRecognition = requestPermission
                )
            }
        }
    }

}
