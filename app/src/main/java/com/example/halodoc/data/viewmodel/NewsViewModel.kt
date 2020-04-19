package com.example.halodoc.data.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.halodoc.base.BaseViewModel
import com.example.halodoc.model.NewsModel
import com.example.halodoc.network.APIResponseListener
import com.example.halodoc.network.ServiceUtil
import com.example.halodoc.rxjavaschedulers.SchedulerProviderImpl
import com.example.halodoc.util.APIEndPoints
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody


class NewsViewModel(private val serviceUtil: ServiceUtil,
                    private val schedulerProviderImpl:SchedulerProviderImpl): BaseViewModel(), APIResponseListener {

    private var newsLiveData: MutableLiveData<NewsModel> ? = null

    private val cache: HashMap<String, NewsModel> = HashMap()

    private val cacheSize = 30

    fun setCache(query: String, page: String, newsModel: NewsModel){
        if(cache.size < cacheSize) {
            val key = "$query-$page"
            if (!cache.containsKey(key)) {
                cache.put(key, newsModel)
            }
        }
    }

    fun observeNewsDataFromServer(): MutableLiveData<NewsModel>?{
        newsLiveData = MutableLiveData()
        return newsLiveData
    }

    fun getNewsData(query: String, page: String) {
        //check data in memory cache
        val key = "$query-$page"
        if(cache.contains(key)){
            newsLiveData?.value = cache.get(query)
        }
        else networkDisposable.add(getNewsDataObservable(query, page))
    }

    private fun getNewsDataObservable(query :String, page:String): Disposable{
        return serviceUtil.getNews(query, page)
                .observeOn(schedulerProviderImpl.ui())
                .subscribeOn(schedulerProviderImpl.io())
                .subscribe {
                    if (it.isSuccessful) onSuccess(APIEndPoints.apiSearch, it.body())
                    else onError(APIEndPoints.apiSearch, it.errorBody()) }
    }


    private fun onHandleError(){
       Log.v("Error", "api failed")
    }

    //we get the response of api's here and then we process according to url's
    override fun onSuccess(reqCode: String?, responseObject: Any?) {
        when(reqCode){

            APIEndPoints.apiSearch -> {
                if(responseObject is NewsModel){
                    newsLiveData?.value = responseObject
                }
            }
        }
    }

    override fun onError(reqCode: String?, errorBody: ResponseBody?) {
            //error handling
        onHandleError()
    }


}