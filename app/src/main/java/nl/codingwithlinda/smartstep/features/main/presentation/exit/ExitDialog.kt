package nl.codingwithlinda.smartstep.features.main.presentation.exit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import nl.codingwithlinda.smartstep.R
import nl.codingwithlinda.smartstep.design.ui.theme.textSecondary

@Composable
fun ExitDialog(
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Surface(
            modifier = modifier
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(48.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.power__turn_on),
                    contentDescription = "turn power off",
                    modifier = Modifier.size(48.dp)
                )

                Text(
                    "The app will fully close and will not run in the background.",
                    color = textSecondary,
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = {
                        onClick()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("OK")
                }
            }
        }
    }
}