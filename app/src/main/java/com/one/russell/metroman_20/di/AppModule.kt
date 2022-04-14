package com.one.russell.metroman_20.di

import com.one.russell.metroman_20.StartViewModel
import com.one.russell.metroman_20.data.prefs.PreferencesDataStore
import com.one.russell.metroman_20.domain.TrainingProcessor
import com.one.russell.metroman_20.domain.providers.BeatTypesProvider
import com.one.russell.metroman_20.domain.providers.BpmProvider
import com.one.russell.metroman_20.domain.providers.ClickStateProvider
import com.one.russell.metroman_20.domain.providers.TrainingDataProvider
import com.one.russell.metroman_20.domain.usecases.*
import com.one.russell.metroman_20.domain.usecases.training.*
import com.one.russell.metroman_20.domain.workers.ClickWorker
import com.one.russell.metroman_20.domain.wrappers.Clicker
import com.one.russell.metroman_20.domain.wrappers.ClickerCallback
import com.one.russell.metroman_20.presentation.screens.main.MainViewModel
import com.one.russell.metroman_20.presentation.screens.training.bar_dropping_subtype_selection.BarDroppingSubtypeSelectionViewModel
import com.one.russell.metroman_20.presentation.screens.training.options.OptionsViewModel
import com.one.russell.metroman_20.presentation.screens.training.tempo_increasing_subtype_selection.TempoIncreasingSubtypeSelectionViewModel
import com.one.russell.metroman_20.presentation.screens.training.type_selection.TypeSelectionViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

fun appModule() = module {

    single { ClickerCallback() }
    single { Clicker(callback = get(), beatTypesProvider = get(), bpmProvider = get(), androidContext().assets) }
    single { TrainingProcessor(clicker = get()) }
    single { PreferencesDataStore(androidContext()) }

    // providers
    single { ClickStateProvider() }
    single { TrainingDataProvider() }
    single { BeatTypesProvider() }
    single { BpmProvider() }

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
    factory { RestoreSavedValuesUseCase(bpmProvider = get(), dataStore = get()) }
    factory { ObserveBpmUseCase(bpmProvider = get()) }
    factory { StartClickingUseCase(androidContext()) }
    factory { StopClickingUseCase(androidContext()) }
    factory { ObserveClickStateUseCase(clickStateProvider = get()) }
    factory { GetClickerCallbackUseCase(clickerCallback = get()) }
    factory { PlayRotateClickUseCase(clicker = get()) }
    factory { SetBpmUseCase(bpmProvider = get(), dataStore = get()) }

    // training use cases
    factory { StartTrainingUseCase(bpmProvider = get(), trainingProcessor = get(), startClickingUseCase = get(), clickStateProvider = get()) }
    factory { StopTrainingUseCase(trainingProcessor = get()) }
    factory { SetTrainingDataUseCase(dataStore = get(), trainingDataProvider = get()) }
    factory { GetTrainingDataUseCase(dataStore = get()) }
    factory { ObserveTrainingDataUseCase(trainingDataProvider = get()) }
    factory { ObserveTrainingStateUseCase(trainingProcessor = get()) }

    // view models
    viewModel {
        StartViewModel(
            restoreSavedValuesUseCase = get()
        )
        MainViewModel(
            playRotateClickUseCase = get(),
            setBpmUseCase = get(),
            observeBpmUseCase = get(),
            startClickingUseCase = get(),
            stopClickingUseCase = get(),
            observeClickStateUseCase = get(),
            getClickerCallbackUseCase = get(),
            observeTrainingDataUseCase = get(),
            startTrainingUseCase = get(),
            stopTrainingUseCase = get(),
            observeTrainingStateUseCase = get()
        )
    }
    viewModel { TypeSelectionViewModel() }
    viewModel { TempoIncreasingSubtypeSelectionViewModel() }
    viewModel { BarDroppingSubtypeSelectionViewModel() }
    viewModel {
        OptionsViewModel(
            clicker = get(),
            setTrainingDataUseCase = get(),
            getTrainingDataUseCase = get()
        )
    }
}