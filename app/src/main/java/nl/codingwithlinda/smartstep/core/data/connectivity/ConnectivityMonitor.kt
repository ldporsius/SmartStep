package nl.codingwithlinda.smartstep.core.data.connectivity

import kotlinx.coroutines.flow.StateFlow

interface ConnectivityMonitor {

    val isConnected: StateFlow<Boolean>

}