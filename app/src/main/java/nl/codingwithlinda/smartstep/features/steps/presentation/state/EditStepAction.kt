package nl.codingwithlinda.smartstep.features.steps.presentation.state

sealed interface EditStepAction {
    data class InputYear(val year: Int): EditStepAction
    data class InputMonth(val month: Int): EditStepAction
    data class InputDay(val day: Int): EditStepAction
    data class SetSteps(val steps: String): EditStepAction
}