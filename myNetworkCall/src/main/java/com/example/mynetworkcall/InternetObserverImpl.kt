package com.example.mynetworkcall



import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class InternetObserverImpl:InternetObserverInterface {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun internetConnectivityListener(
        lifecycleScope: LifecycleCoroutineScope,
        onAvailable: (it: String) -> Unit,
        onUnAvailable: (it: String) -> Unit,
        onLosing: (it: String) -> Unit,
        onLost: (it: String) -> Unit,
        context: Context,
    ) {
        val connectivityObserver = NetworkConnectivityObserver(context)
        connectivityObserver.observe().onEach {
            when (it) {
                ConnectivityObserver.Status.Available -> onAvailable(it.name)
                ConnectivityObserver.Status.Unavailable -> onUnAvailable(it.name)
                ConnectivityObserver.Status.Losing -> onLosing(it.name)
                ConnectivityObserver.Status.Lost -> onLost(it.name)
            }
        }.launchIn(lifecycleScope)
    }
}

@ExperimentalCoroutinesApi
private class NetworkConnectivityObserver(
    context: Context
) : ConnectivityObserver {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<ConnectivityObserver.Status> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch {
                        send(ConnectivityObserver.Status.Available)
                    }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch {
                        send(ConnectivityObserver.Status.Losing)
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch {
                        send(ConnectivityObserver.Status.Lost)
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch {
                        send(ConnectivityObserver.Status.Unavailable)
                    }
                }
            }
            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
    }
}


private interface ConnectivityObserver {
    fun observe(): Flow<Status>
    enum class Status {
        Available, Unavailable, Losing, Lost
    }
}