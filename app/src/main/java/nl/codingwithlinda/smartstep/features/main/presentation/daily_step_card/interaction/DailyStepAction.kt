package nl.codingwithlinda.smartstep.features.main.presentation.daily_step_card.interaction

sealed interface DailyStepAction {
    data object ActionEdit: DailyStepAction
    data object ActionPause: DailyStepAction
    data object ActionPlay: DailyStepAction
    data object ActionReport: DailyStepAction

}