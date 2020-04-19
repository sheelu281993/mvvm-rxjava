package com.example.halodoc.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    var networkDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        clearDisposable()
        super.onCleared()
    }

    private fun clearDisposable(){
        if(!networkDisposable.isDisposed) networkDisposable.dispose()
    }

}