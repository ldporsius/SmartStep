package nl.codingwithlinda.smartstep.features.main.presentation.permissions

import android.Manifest
import android.R.attr.onClick
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
    private val actionBackgroundIntent = ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS

    val permissionUiState = MutableStateFlow(PermissionUiState.NA)
    fun setPermissionState(uiState: PermissionUiState) {
        permissionUiState.update {
           uiState
        }
    }

    val shouldShowBottomSheet = permissionUiState.map {
        it != PermissionUiState.NA
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)


    @Composable
    fun BottomSheetContent(
        permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
        batteryOptimizeLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    ) {

        val context = LocalActivity.current
        val state: PermissionUiState = this.permissionUiState.collectAsStateWithLifecycle().value
        when (state) {
            PermissionUiState.NA -> Unit
            PermissionUiState.BACKGROUND_ACCESS_RECOMMENDED -> {
                BackgroundAccessRecommendedDialog(
                    onClick = {
                        context?.let {
                            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                                data = Uri.parse("package:${context.packageName}")
                            }

                            batteryOptimizeLauncher.launch(intent)
                        }
                    },
                    modifier = Modifier
                        .width(480.dp)
                        .padding(48.dp)
                )
            }
            PermissionUiState.DENIED -> {
                BodySensorsPermissionRationaleDialog(
                    onClick = {
                        permissionLauncher.launch(Manifest.permission.BODY_SENSORS)
                    },
                    modifier = Modifier
                        .width(480.dp)
                        .padding(48.dp)
                )
            }
            PermissionUiState.DENIED_PERMANENTLY -> {
                BodySensorsPermissionDeclinedDialog(
                    onClick = {
                        context?.openAppSettings()
                    },
                    modifier = Modifier
                        .width(480.dp)
                        .padding(48.dp)
                )
            }
        }
    }
}

fun isIgnoringBatteryOptimizations(context: Context): Boolean {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    return powerManager.isIgnoringBatteryOptimizations(context.packageName)
}