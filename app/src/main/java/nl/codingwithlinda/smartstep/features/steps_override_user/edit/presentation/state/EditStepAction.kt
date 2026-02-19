package nl.codingwithlinda.smartstep.features.steps_override_user.edit.presentation.state

sealed interface EditStepAction {
    object ShowDatePicker: EditStepAction
    object DismissDatePicker: EditStepAction
    data class InputYear(val year: Int): EditStepAction
    data class InputMonth(val month: Int): EditStepAction
    data class InputDay(val day: Int): EditStepAction
    data class SetSteps(val steps: String): EditStepAction

    data object Save: EditStepAction

}