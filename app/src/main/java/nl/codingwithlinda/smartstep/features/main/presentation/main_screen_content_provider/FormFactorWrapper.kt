package nl.codingwithlinda.smartstep.features.main.presentation.main_screen_content_provider

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.window.Dialog
import nl.codingwithlinda.smartstep.design_system.components.CustomBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormFactorWrapper(
    onDismiss: () -> Unit,
    useCustomBottomSheet: Boolean = false,
    content: @Composable () -> Unit,
) {

    val density = LocalDensity.current.density
    val isLargeScreen = LocalWindowInfo.current.containerSize.width > 840 * density

    if (isLargeScreen){
        Dialog(
            onDismissRequest = { onDismiss()}
        ) {
            content()
        }
    }
    else{
        if (useCustomBottomSheet){
            CustomBottomSheet(
                onDismiss = { onDismiss()}
            ) {
                content()
            }
        }
        else {
            ModalBottomSheet(
                onDismissRequest = { onDismiss() }
            ) {
                content()
            }
        }
    }

}