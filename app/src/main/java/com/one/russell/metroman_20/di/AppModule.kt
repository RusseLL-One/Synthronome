package com.one.russell.metroman_20.di

import android.content.Context
import com.one.russell.metroman_20.data.prefs.PreferencesDataStore
import com.one.russell.metroman_20.domain.providers.ClickStateProvider
import com.one.russell.metroman_20.domain.usecases.*
import com.one.russell.metroman_20.domain.workers.ClickWorker
import com.one.russell.metroman_20.domain.wrappers.Clicker
import com.one.russell.metroman_20.presentation.screens.main.MainViewModel
import com.one.russell.metroman_20.presentation.screens.training_type_selection.TrainingTypeSelectionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

fun appModule(
    appContext: Context
) = module {

    // singletons
    single { Clicker(appContext.assets) }
    single { PreferencesDataStore(appContext) }
    single { ClickStateProvider() }

    // workers
    worker {
        ClickWorker(
            appContext = get(),
            workerParams = get(),
            clicker = get(),
            clickStateProvider = get()
        )
    }

    // use cases
    factory { GetClickerUseCase(clicker = get()) }
    factory { GetSavedBpmUseCase(preferencesDataStore = get()) }
    factory { SaveCurrentBpmUseCase(preferencesDataStore = get()) }
    factory { StartClickingUseCase(appContext) }
    factory { StopClickingUseCase(appContext) }
    factory { ObserveClickStateUseCase(clickStateProvider = get()) }

    // view models
    viewModel {
        MainViewModel(
            getClickerUseCase = get(),
            getSavedBpmUseCase = get(),
            saveCurrentBpmUseCase = get(),
            startClickingUseCase = get(),
            stopClickingUseCase = get(),
            observeClickStateUseCase = get()
        )
        TrainingTypeSelectionViewModel()
    }
}