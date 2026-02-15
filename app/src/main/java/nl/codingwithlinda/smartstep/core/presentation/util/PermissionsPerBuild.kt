package nl.codingwithlinda.smartstep.core.presentation.util

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build

@SuppressLint("InlinedApi")
fun permissionsPerBuild(BuildVersion: Int): List<String>{
   return when{
        BuildVersion >= Build.VERSION_CODES.TIRAMISU -> listOf(
            Manifest.permission.ACTIVITY_RECOGNITION,
            //Manifest.permission.POST_NOTIFICATIONS
        )
        BuildVersion >= Build.VERSION_CODES.O -> listOf(
            Manifest.permission.ACTIVITY_RECOGNITION,
        )
       else -> emptyList()
    }
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