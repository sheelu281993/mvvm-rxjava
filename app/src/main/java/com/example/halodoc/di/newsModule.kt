package com.example.halodoc.di

import com.example.halodoc.rxjavaschedulers.SchedulerProviderImpl
import com.example.halodoc.data.viewmodel.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val trueCallerBlogModule = module {

    single{ SchedulerProviderImpl() }

    viewModel {
        NewsViewModel(get(), get())
    }

}

