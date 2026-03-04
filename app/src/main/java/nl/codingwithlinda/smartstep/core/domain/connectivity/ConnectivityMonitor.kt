package nl.codingwithlinda.smartstep.core.domain.connectivity

import kotlinx.coroutines.flow.StateFlow

interface ConnectivityMonitor {

    val isConnected: StateFlow<Boolean>

}