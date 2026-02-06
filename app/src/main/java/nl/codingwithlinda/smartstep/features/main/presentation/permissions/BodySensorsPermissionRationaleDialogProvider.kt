package nl.codingwithlinda.smartstep.features.main.presentation.permissions

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.core.presentation.util.openAppSettings
import nl.codingwithlinda.smartstep.design.ui.theme.textPrimary
import nl.codingwithlinda.smartstep.design.ui.theme.textSecondary

class BodySensorsPermissionRationaleDialogProvider(
    private val onClick: () -> Unit
): PermissionDialogProvider {

    @Composable
    override fun Description() {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(painter = painterResource(R.drawable.running), contentDescription = "image_track")
            Text(
                "To count your steps, SmartStep needs access to your motion sensors.",
                textAlign = TextAlign.Center,
                color = textPrimary
            )

            val activity = LocalActivity.current
            activity?.let {
                Button(onClick = {
                    onClick()
                }) {
                    Text("Allow access")
                }
            }
        }

    }
}