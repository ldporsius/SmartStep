package nl.codingwithlinda.smartstep.features.main.presentation.permissions

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import nl.codingwithlinda.smartstep.core.presentation.util.openAppSettings

class PermissionsViewModel: ViewModel() {

    val permissionUiState = MutableStateFlow(PermissionUiState.NA)
    fun setPermissionState(uiState: PermissionUiState) {
        permissionUiState.update {
           uiState
        }
    }

    val shouldShowUserInteraction = permissionUiState.map {
        it != PermissionUiState.NA
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)




    @Composable
    fun BottomSheetContent(
        requestActivityRecognition: () -> Unit

    ) {

        val context = LocalActivity.current
        val state: PermissionUiState = this.permissionUiState.collectAsStateWithLifecycle().value
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
}

