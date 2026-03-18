package nl.codingwithlinda.smartstep.features.ai_integration.presentation.error

import nl.codingwithlinda.ai.domain.error.AIError
import nl.codingwithlinda.smartstep.core.presentation.util.UiText

fun AIError.toUIString(): String{
    return when(this){
        AIError.OtherError -> ("Something went wrong")
        is AIError.ResourceExhausted -> ("Exceeded quota")

        else ->("Oops")
    }
}