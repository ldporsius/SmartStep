package nl.codingwithlinda.smartstep.features.main.presentation.permissions

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import nl.codingwithlinda.smartstep.core.presentation.util.necessaryPermissionsOnly
import nl.codingwithlinda.smartstep.core.presentation.util.permissionsPerBuild

enum class PermissionUiState {
    NA,
    DENIED_ACTIVITY_RECOGNITION,
    DENIED_ACTIVITY_RECOGNITION_PERMANENTLY,
    DENIED_NOTIFICATIONS,
    DENIED_NOTIFICATIONS_PERMANENTLY
}

fun Activity.toPermissionUiState(permission: String): PermissionUiState{
    if (permission == android.Manifest.permission.ACTIVITY_RECOGNITION)
    {
        val shouldShowRationale = shouldShowRequestPermissionRationale(permission)

        return if (shouldShowRationale) PermissionUiState.DENIED_ACTIVITY_RECOGNITION
        else PermissionUiState.DENIED_ACTIVITY_RECOGNITION_PERMANENTLY
    }

    return PermissionUiState.NA
}

fun Activity.canStartStepTrackerService(): Boolean{
    val permsNeeded = necessaryPermissionsOnly()
    if (permsNeeded.isEmpty()) return true
    val allGranted = permsNeeded.map {
        checkSelfPermission(it)
    }.all {
        it == PackageManager.PERMISSION_GRANTED
    }

    return allGranted
}


