package com.example.halodoc.util

import android.content.Context
import android.net.ConnectivityManager

class Utils {

    companion object {
        fun isInternetConnected(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
            return false
        }
    }
}