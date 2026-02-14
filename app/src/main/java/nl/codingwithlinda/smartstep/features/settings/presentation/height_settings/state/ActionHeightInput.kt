package nl.codingwithlinda.smartstep.features.settings.presentation.height_settings.state

import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystems

sealed interface ActionHeightInput {
    data class CmInput(val cm: Int): ActionHeightInput
    data class ImperialInput(val feet: Int, val inches: Int): ActionHeightInput
    data object ActionSave: ActionHeightInput
    data class ChangeUnitSystem(val system: UnitSystems): ActionHeightInput
}
