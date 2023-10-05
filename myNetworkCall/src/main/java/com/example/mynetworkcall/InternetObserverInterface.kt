package com.example.mynetworkcall

import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi

interface InternetObserverInterface {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun internetConnectivityListener(
        lifecycleScope: LifecycleCoroutineScope,
        onAvailable: (it: String) -> Unit,
        onUnAvailable: (it: String) -> Unit = {},
        onLosing: (it: String) -> Unit = {},
        onLost: (it: String) -> Unit,
        context: Context,
    )
}