package com.sample.desafio.data.device

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidNetworkHandler @Inject constructor(
    @ApplicationContext private val context: Context
) : NetworkHandler {
    override fun isConnected(): Boolean {
        val connectivityManager = context.connectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }
}


val Context.connectivityManager: ConnectivityManager
    get() = (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)