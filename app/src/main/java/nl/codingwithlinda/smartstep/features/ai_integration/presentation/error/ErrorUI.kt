package nl.codingwithlinda.smartstep.features.ai_integration.presentation.error

import nl.codingwithlinda.ai.domain.error.AIError
import nl.codingwithlinda.core.domain.util.UiText

fun AIError.toUI(): UiText{
    return when(this){
        AIError.OtherError -> UiText.DynamicText("Something went wrong")
        is AIError.ResourceExhausted -> UiText.DynamicText("Exceeded quota")

        else -> UiText.DynamicText("Oops")
    }
}

fun AIError.toUIString(): String{
    return when(this){
        AIError.OtherError -> ("Something went wrong")
        is AIError.ResourceExhausted -> ("Exceeded quota")

        else ->("Oops")
    }
}