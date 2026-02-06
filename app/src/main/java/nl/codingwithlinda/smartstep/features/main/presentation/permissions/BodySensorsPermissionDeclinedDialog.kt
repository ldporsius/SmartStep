package nl.codingwithlinda.smartstep.features.main.presentation.permissions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.design.ui.theme.textSecondary

@Composable
fun BodySensorsPermissionDeclinedDialog(
    onClick: () -> Unit,
    modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(
            "Enable access manually",
            style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            "To track your steps, please enable the permission in your device settings.",
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            color = textSecondary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.align(Alignment.Start),
            horizontalAlignment = androidx.compose.ui.Alignment.Start,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            Text("1. Open Permissions")
            Text("2. Tap Physical activity")
            Text("3. Select Allow")

        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            onClick()
        }) {
            Text("Open Settings")
        }
    }

}

@Preview
@Composable
private fun PreviewBodySensorsPermissionDeclinedDialog() {
    BodySensorsPermissionDeclinedDialog(
        onClick = {},
        modifier = Modifier.width(400.dp).padding(16.dp)

    )

}