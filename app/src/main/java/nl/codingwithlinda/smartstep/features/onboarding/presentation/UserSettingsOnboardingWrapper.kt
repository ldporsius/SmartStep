package nl.codingwithlinda.smartstep.features.onboarding.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import nl.codingwithlinda.smartstep.application.SmartStepApplication
import nl.codingwithlinda.smartstep.application.SmartStepApplication.Companion.userSettingsRepo
import nl.codingwithlinda.smartstep.features.settings.data.UserSettingsMemento
import nl.codingwithlinda.smartstep.navigation.MainRoute
import nl.codingwithlinda.smartstep.navigation.NavigationController

@Composable
fun UserSettingsOnboardingWrapper(
    modifier: Modifier = Modifier,
    onSkip:() -> Unit,
    action: () -> Unit,
    content: @Composable () -> Unit,

) {
    Column(
        modifier = modifier,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
    ) {
        UserSettingsOnboardingHeader(
            onSkip = onSkip
        )
        content()
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                action()
            },
            modifier = Modifier.width(480.dp)
                .padding(16.dp)
        ) {
            Text("Start")
        }
    }
}