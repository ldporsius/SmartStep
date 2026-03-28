package nl.codingwithlinda.smartstep.design_system.form_factors

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass


@Composable
fun screenFormHelper(): ScreenInfo {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    val isLargeWidth = windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)
    val isMediumWidth =
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
    val isMediumHeight =
        windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)

    if (!isMediumHeight)
        return ScreenInfo(
            orientation = ScreenOrientation.LANDSCAPE,
            form = ScreenForm.PHONE
        )


    if (isLargeWidth)
        return ScreenInfo(
            orientation = ScreenOrientation.NA,
            form = ScreenForm.DESKTOP
        )

    val screenForm = when (isMediumWidth) {
        true -> {
            ScreenInfo(
                orientation = ScreenOrientation.PORTRAIT,
                form = ScreenForm.TABLET
            )
        }

        false -> {
            ScreenInfo(
                orientation = ScreenOrientation.PORTRAIT,
                form = ScreenForm.PHONE
            )
        }
    }
    return screenForm
}