package com.one.russell.metroman_20.di

import android.content.Context
import com.one.russell.metroman_20.data.prefs.PreferencesDataStore
import com.one.russell.metroman_20.domain.usecases.GetClickerUseCase
import com.one.russell.metroman_20.domain.usecases.GetSavedBpmUseCase
import com.one.russell.metroman_20.domain.usecases.SaveCurrentBpmUseCase
import com.one.russell.metroman_20.domain.wrappers.Clicker
import com.one.russell.metroman_20.presentation.screens.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun appModule(
    appContext: Context
) = module {

    // singletons
    single { Clicker(appContext.assets) }
    single { PreferencesDataStore(appContext) }

    // use cases
    factory { GetClickerUseCase(clicker = get()) }
    factory { GetSavedBpmUseCase(preferencesDataStore = get()) }
    factory { SaveCurrentBpmUseCase(preferencesDataStore = get()) }

    // view models
    viewModel {
        MainViewModel(
            getClickerUseCase = get(),
            getSavedBpmUseCase = get(),
            saveCurrentBpmUseCase = get()
        )
    }
}