package com.example.halodoc.network


import com.example.halodoc.model.NewsModel
import com.example.halodoc.util.APIEndPoints
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceUtil {

    @GET(APIEndPoints.apiSearch)
    fun getNews(@Query("query") searhQuery: String, @Query("page") page : String): Observable<Response<NewsModel>>

}
