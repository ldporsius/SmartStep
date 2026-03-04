package nl.codingwithlinda.smartstep.core.data.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import nl.codingwithlinda.smartstep.core.domain.connectivity.ConnectivityMonitor

class ConnectivityCheck (
    private val context: Context
) : ConnectivityMonitor, ConnectivityManager.NetworkCallback(){

    private fun connectivityManager() = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()
    private val _isConnected = MutableStateFlow(false)
    override val isConnected = _isConnected.asStateFlow()


    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        _isConnected.value = true

    }

    override fun onLost(network: Network) {
        super.onLost(network)
        _isConnected.value = false
    }


    init {
       connectivityManager().requestNetwork(networkRequest, this)
    }
}