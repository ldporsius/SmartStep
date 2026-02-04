package nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state

import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystemUnits

interface ActionWeightInput {
    data class PoundsInput(val pounds: Int): ActionWeightInput
    data class KgInput(val kg: Int): ActionWeightInput
    data object Save: ActionWeightInput
    data class ChangeSystem(val system: UnitSystemUnits): ActionWeightInput

}