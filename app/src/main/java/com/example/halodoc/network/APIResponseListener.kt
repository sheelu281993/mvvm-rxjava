package com.example.halodoc.network

import okhttp3.ResponseBody

interface APIResponseListener {

    fun onSuccess(reqCode: String?, responseObject: Any?)
    fun onError(reqCode: String?, errorBody: ResponseBody?)
}