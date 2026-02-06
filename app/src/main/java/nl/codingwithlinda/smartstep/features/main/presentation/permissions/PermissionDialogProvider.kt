package nl.codingwithlinda.smartstep.features.main.presentation.permissions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface PermissionDialogProvider {
    @Composable
    fun Description(modifier: Modifier = Modifier)
}