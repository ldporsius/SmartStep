package nl.codingwithlinda.smartstep.features.main.presentation.permissions

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import nl.codingwithlinda.smartstep.core.presentation.util.openAppSettings
import nl.codingwithlinda.smartstep.design.ui.theme.textSecondary

class BodySensorsPermissionDeclinedDialogProvider: PermissionDialogProvider {
        @Composable
    override fun Description() {
        Column(
            modifier = Modifier,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Text("Enable access manually",
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center)

            Text(
                "To track your steps, please enable the permission in your device settings.",
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = textSecondary
            )

            Text("1. Open Permissions")
            Text("2. Tap Physical activity")
            Text("3. Select Allow")

            val activity = LocalActivity.current
            activity?.let {
                Button(onClick = {
                    it.openAppSettings()
                }) {
                    Text("Open Settings")
                }
            }
        }

    }
}