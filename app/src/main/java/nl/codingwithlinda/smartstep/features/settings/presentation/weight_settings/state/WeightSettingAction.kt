package nl.codingwithlinda.smartstep.features.settings.presentation.weight_settings.state

import nl.codingwithlinda.smartstep.core.domain.unit_conversion.UnitSystemUnits

interface WeightSettingAction {
    data class PoundsInput(val pounds: Int): WeightSettingAction
    data class KgInput(val kg: Int): WeightSettingAction
    data object Save: WeightSettingAction
    data class ChangeSystem(val system: UnitSystemUnits): WeightSettingAction

}