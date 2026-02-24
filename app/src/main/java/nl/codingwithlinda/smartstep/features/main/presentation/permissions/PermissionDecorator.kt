package nl.codingwithlinda.smartstep.features.main.presentation.permissions

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.codingwithlinda.smartstep.core.presentation.util.openAppSettings
import nl.codingwithlinda.smartstep.features.main.navigation.controller.MainNavAction
import nl.codingwithlinda.smartstep.features.main.navigation.nav_drawer_events.controllers.MainNavActionControllerImpl
import nl.codingwithlinda.smartstep.features.main.navigation.nav_drawer_events.controllers.MainNavItemHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDecorator(
    permissionsViewModel: PermissionsViewModel,
    navItemHandler: MainNavActionControllerImpl = MainNavItemHandler,
    requestPermission: () -> Unit
    ) {

    val density = LocalDensity.current.density
    val isLargeScreen = LocalWindowInfo.current.containerSize.width > 840 * density

    @Composable
    fun BottomSheetContent(
        requestActivityRecognition: () -> Unit
    ) {

        val context = LocalActivity.current
        val state: PermissionUiState = permissionsViewModel.permissionUiState.collectAsStateWithLifecycle().value
        when (state) {
            PermissionUiState.NA -> Unit

            PermissionUiState.DENIED_ACTIVITY_RECOGNITION -> {
                BodySensorsPermissionRationaleDialog(
                    onClick = {
                        requestActivityRecognition()
                    },
                    modifier = Modifier
                        .width(480.dp)
                        .padding(48.dp)
                )
            }
            PermissionUiState.DENIED_ACTIVITY_RECOGNITION_PERMANENTLY -> {
                BodySensorsPermissionDeclinedDialog(
                    onClick = {
                        context?.openAppSettings()
                    },
                    modifier = Modifier
                        .width(480.dp)
                        .padding(48.dp)
                )
            }
            else -> Unit
        }
    }


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
                    BottomSheetContent(
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
                BottomSheetContent(
                    requestActivityRecognition = requestPermission
                )
            }
        }
    }

}
