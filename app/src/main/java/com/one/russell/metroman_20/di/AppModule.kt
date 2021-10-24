package com.one.russell.metroman_20.di

import android.content.Context
import com.one.russell.metroman_20.domain.usecases.GetClickerUseCase
import com.one.russell.metroman_20.domain.wrappers.Clicker
import com.one.russell.metroman_20.presentation.screens.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun appModule(
    appContext: Context
) = module {

    // singletons
    single { Clicker(appContext.assets) }

    // use cases
    factory { GetClickerUseCase(clicker = get()) }

    // view models
    viewModel {
        MainViewModel(getClickerUseCase = get())
    }
}