package nl.codingwithlinda.smartstep.design_system.form_factors


enum class ScreenOrientation {
    PORTRAIT,
    LANDSCAPE,
    NA
}
enum class ScreenForm{
    PHONE,
    TABLET,
    DESKTOP
}

data class ScreenInfo(
    val orientation: ScreenOrientation,
    val form: ScreenForm

)