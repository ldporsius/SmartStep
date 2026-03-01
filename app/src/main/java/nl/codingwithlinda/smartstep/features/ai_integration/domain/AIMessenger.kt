package nl.codingwithlinda.smartstep.features.ai_integration.domain

import kotlinx.coroutines.flow.Flow

interface AIMessenger {

    fun create(text: String): AIMessage
    fun send(message: AIMessage)
    fun receive(text: String)
    val messages: Flow<List<AIMessage>>
}

