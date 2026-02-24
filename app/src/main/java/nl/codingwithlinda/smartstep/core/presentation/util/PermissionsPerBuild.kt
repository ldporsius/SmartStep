package nl.codingwithlinda.smartstep.core.presentation.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import nl.codingwithlinda.smartstep.features.main.presentation.battery_optimization.isIgnoringBatteryOptimizations

@SuppressLint("InlinedApi")
fun permissionsPerBuild(BuildVersion: Int): List<String>{
   return when{
        BuildVersion >= Build.VERSION_CODES.TIRAMISU -> listOf(
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.POST_NOTIFICATIONS
        )
        BuildVersion >= Build.VERSION_CODES.O -> listOf(
            Manifest.permission.ACTIVITY_RECOGNITION,
        )
       else -> emptyList()
    }
}

fun Activity.canStartStepTrackerLimited(): Boolean{
    val permsNeeded = necessaryPermissionsOnly()
    if (permsNeeded.isEmpty()) return true
    val allGranted = permsNeeded.map {
        checkSelfPermission(it)
    }.all {
        it == PackageManager.PERMISSION_GRANTED
    }

    return allGranted
}
fun Activity.canStartStepTrackerService(): Boolean{
    val canRunInBackground = isIgnoringBatteryOptimizations(this)
    return canStartStepTrackerLimited() && canRunInBackground
}

@SuppressLint("InlinedApi")
enum class PermissionCode(val code: String){
    ACTIVITY_RECOGNITION(Manifest.permission.ACTIVITY_RECOGNITION),
    POST_NOTIFICATIONS(Manifest.permission.POST_NOTIFICATIONS)
}

fun BuildVersionNeedsPermission(permissionCode: PermissionCode): Boolean{
    val build = Build.VERSION.SDK_INT
    return permissionsPerBuild(build).contains(permissionCode.code)
}

@SuppressLint("InlinedApi")
fun necessaryPermissionsOnly() = permissionsPerBuild(Build.VERSION.SDK_INT).filter {
    it == Manifest.permission.ACTIVITY_RECOGNITION
}

@SuppressLint("InlinedApi")
fun permissionsNeededForForgroundService() = permissionsPerBuild(Build.VERSION.SDK_INT).filter {
    it == Manifest.permission.POST_NOTIFICATIONS
}