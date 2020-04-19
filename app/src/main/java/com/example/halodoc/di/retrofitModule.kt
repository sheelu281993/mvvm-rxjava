package com.example.halodoc.di

import com.example.halodoc.network.ServiceUtil
import com.example.halodoc.util.APIEndPoints
import com.example.halodoc.util.Constants.*
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val retrofitModule = module {

    single<Call.Factory> {
        okhttp()
    }
    single {
        retrofit(get(), baseUrl = APIEndPoints.baseUrl)
    }
    single {
        get<Retrofit>().create(ServiceUtil::class.java)
    }
}

private val loggingInterceptor: Interceptor
    get() = HttpLoggingInterceptor().apply { HttpLoggingInterceptor.Level.BODY }


//private val headerInterceptor: Interceptor
//    get() = object: Interceptor{
//        override fun intercept(chain: Interceptor.Chain): Response {
//            val original = chain.request()
//            val builder = original.newBuilder()
//            builder.method(original.method, original.body)
//            val request = builder.build()
//            return chain.proceed(request)
//        }
//    }

private fun okhttp() = OkHttpClient.Builder()
        .connectTimeout(CONNECT_TIMEOUT_SECS.toLong(), java.util.concurrent.TimeUnit.SECONDS).writeTimeout(CONNECT_TIMEOUT_SECS.toLong(), java.util.concurrent.TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()



private fun retrofit(callFactory: Call.Factory, baseUrl: String) = Retrofit.Builder()
        .callFactory(callFactory)
        .baseUrl(baseUrl)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()



