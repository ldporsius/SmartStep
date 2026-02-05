package nl.codingwithlinda.smartstep.features.main.presentation.permissions

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import nl.codingwithlinda.smartstep.core.presentation.util.openAppSettings

@Composable
fun PermissionDialog(
    permissionDialogProvider: PermissionDialogProvider,
    isPermanentlyDeclined: Boolean,
    grantPermission: () -> Unit,
    modifier: Modifier = Modifier) {

    val activity = LocalActivity.current
    Column {
        Text(permissionDialogProvider.description(isPermanentlyDeclined))

        if (isPermanentlyDeclined){
            activity?.let {
                Button(onClick = {
                    it.openAppSettings()
                }) {
                    Text("Open Settings")
                }
            }
        }
        else{
            Button(onClick = {
                grantPermission()
            }) {
                Text("OK")
            }

        }
    }
}