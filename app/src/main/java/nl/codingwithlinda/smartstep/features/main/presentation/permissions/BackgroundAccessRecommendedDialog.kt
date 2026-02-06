package nl.codingwithlinda.smartstep.features.main.presentation.permissions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import nl.codingwithlinda.smartstep.design.ui.theme.textSecondary


@Composable
fun BackgroundAccessRecommendedDialog(
    onClick: () -> Unit,
    modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

       Text("Background access recommended",
           style = MaterialTheme.typography.titleLarge,
           fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
           textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            "Background access helps Smart Step track your activity more reliably.",
            textAlign = TextAlign.Center,
            color = textSecondary
        )

        Spacer(modifier = Modifier.height(48.dp))

            Button(onClick = {
                onClick()
            }) {
                Text("Continue")
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



