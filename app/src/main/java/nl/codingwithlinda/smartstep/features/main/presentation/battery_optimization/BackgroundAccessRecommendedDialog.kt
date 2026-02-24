package nl.codingwithlinda.smartstep.features.main.presentation.battery_optimization

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import nl.codingwithlinda.smartstep.design_system.ui.theme.textSecondary


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
           fontWeight = FontWeight.Bold,
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


