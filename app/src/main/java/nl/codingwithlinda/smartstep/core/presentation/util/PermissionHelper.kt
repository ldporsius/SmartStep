package nl.codingwithlinda.smartstep.core.presentation.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import kotlin.jvm.java

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity )

}