package nl.codingwithlinda.smartstep.features.ai_integration.features.chat.data.intro_message

import nl.codingwithlinda.ai.domain.model.AIMessage
import nl.codingwithlinda.ai.domain.model.AIMessageOrigin

fun introMessage(
    activityLevelPercentage: Int
) = AIMessage(
    message = """
        Please introduce yourself in one short sentence.
        Comment on the user's progress: $activityLevelPercentage percent of their goal is reached.
        Offer help.
    """.trimIndent(),
    origin = AIMessageOrigin.USER
)