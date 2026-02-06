package nl.codingwithlinda.smartstep.features.main.presentation.permissions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.design.ui.theme.textPrimary


@Composable
fun BodySensorsPermissionRationaleDialog(
    onClick: () -> Unit,
    modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(painter = painterResource(R.drawable.running), contentDescription = "image_track")
        Spacer(modifier = Modifier.weight(1f))

        Text(
            "To count your steps, SmartStep needs access to your motion sensors.",
            textAlign = TextAlign.Center,
            color = textPrimary
        )

        Spacer(modifier = Modifier.weight(1f))

            Button(onClick = {
                onClick()
            }) {
                Text("Allow access")
            }

    }
}

@Preview
@Composable
private fun PreviewBodySensorsPermissionRationaleDialogProvider() {
    BodySensorsPermissionRationaleDialog(
        onClick = {},
        modifier = Modifier.size(240.dp).padding(16.dp)
    )
}



