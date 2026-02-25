package nl.codingwithlinda.smartstep.features.batteryOptimisation.presentation

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AllowBackgroundAccessDialog(
    onResult: (Boolean) -> Unit,
    onDismiss: () -> Unit
){
    val context = LocalActivity.current

    val batteryOptimizeLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            println("--- BATTERY OPTIMIZE LAUNCHER RETURNED WITH RESULT: ${activityResult.resultCode}, ${activityResult.data}")
            onResult(true)

            onDismiss()
        }

        BackgroundAccessRecommendedDialog(
            onClick = {
                context?.let {
                    try {
                        val intent =
                            Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                                data = Uri.parse("package:${context.packageName}")
                            }

                        batteryOptimizeLauncher.launch(intent)
                    }catch (e: Exception){
                        context.let {
                            Toast.makeText(it, "Could not handle intent ignore battery optimizations", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier
                .width(480.dp)
                .padding(48.dp)
        )
}

