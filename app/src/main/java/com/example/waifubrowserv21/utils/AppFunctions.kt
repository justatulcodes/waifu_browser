package com.example.waifubrowserv21.utils


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


/**
 * Stores all functions so main activity is not cluttered.
 */
class AppFunctions {

    /**
     * Checks if internet connectivity is available or not
     */
    fun isInternetAvailable(context: Context) : Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        return capabilities != null &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }






}