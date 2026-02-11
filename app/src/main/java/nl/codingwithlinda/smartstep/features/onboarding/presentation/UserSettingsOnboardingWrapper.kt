package nl.codingwithlinda.smartstep.features.onboarding.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun UserSettingsOnboardingWrapper(
    modifier: Modifier = Modifier,
    onSkip:() -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        UserSettingsOnboardingHeader(
            onSkip = onSkip
        )
        content()
    }
}