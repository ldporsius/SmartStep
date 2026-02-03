package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state

sealed interface ActionUnitInput {
    data class CmInput(val cm: Int): ActionUnitInput
    data class ImperialInput(val feet: Int, val inches: Int): ActionUnitInput
    //data class FeetInput(val feet: Int): ActionUnitInput
    //data class InchesInput(val inches: Int): ActionUnitInput
}