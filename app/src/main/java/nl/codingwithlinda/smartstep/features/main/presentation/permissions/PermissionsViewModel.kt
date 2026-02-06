package nl.codingwithlinda.smartstep.features.main.presentation.permissions

import android.Manifest
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class PermissionsViewModel: ViewModel() {

    val permissionsRequired = listOf<String>(
        Manifest.permission.BODY_SENSORS,
    )
    val actionBackgroundIntent = ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS

    val permissionState = MutableStateFlow(false)
}