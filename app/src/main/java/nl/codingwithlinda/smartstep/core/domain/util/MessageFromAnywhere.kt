package nl.codingwithlinda.smartstep.core.domain.util

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object MessageFromAnywhere {

    private val _msgChannel = Channel<String>()
    val msgChannel = _msgChannel.receiveAsFlow()

    fun sendMsg(msg: String) {
        _msgChannel.trySend(msg)
    }


}