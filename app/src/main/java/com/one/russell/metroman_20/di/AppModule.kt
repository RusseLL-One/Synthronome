package com.one.russell.metroman_20.di

import com.one.russell.metroman_20.StartViewModel
import com.one.russell.metroman_20.data.prefs.PreferencesDataStore
import com.one.russell.metroman_20.domain.TrainingProcessor
import com.one.russell.metroman_20.domain.providers.*
import com.one.russell.metroman_20.domain.usecases.*
import com.one.russell.metroman_20.domain.usecases.colors.ObserveColorsUseCase
import com.one.russell.metroman_20.domain.usecases.colors.SetColorsUseCase
import com.one.russell.metroman_20.domain.usecases.training.*
import com.one.russell.metroman_20.domain.workers.ClickWorker
import com.one.russell.metroman_20.domain.wrappers.Clicker
import com.one.russell.metroman_20.domain.wrappers.ClickerCallback
import com.one.russell.metroman_20.presentation.screens.main.MainViewModel
import com.one.russell.metroman_20.presentation.screens.training.training_subtype_selection.TrainingSubtypeSelectionViewModel
import com.one.russell.metroman_20.presentation.screens.training.options.OptionsViewModel
import com.one.russell.metroman_20.presentation.screens.training.training_type_selection.TrainingTypeSelectionViewModel
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
    single { ColorsProvider() }

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
    factory { RestoreSavedValuesUseCase(bpmProvider = get(), beatTypesProvider = get(), colorsProvider = get(), dataStore = get()) }
    factory { SaveValuesUseCase(bpmProvider = get(), beatTypesProvider = get(), colorsProvider = get(), dataStore = get()) }
    factory { ObserveBpmUseCase(bpmProvider = get()) }
    factory { StartClickingUseCase(androidContext()) }
    factory { StopClickingUseCase(androidContext()) }
    factory { ObserveClickStateUseCase(clickStateProvider = get()) }
    factory { GetClickerCallbackUseCase(clickerCallback = get()) }
    factory { PlayRotateClickUseCase(clicker = get()) }
    factory { SetBpmUseCase(bpmProvider = get()) }
    factory { SetTimeSignatureUseCase(beatTypesProvider = get()) }
    factory { IncrementBeatTypeUseCase(beatTypesProvider = get()) }
    factory { ObserveBeatTypesUseCase(beatTypesProvider = get()) }
    factory { ObserveColorsUseCase(colorsProvider = get()) }
    factory { SetColorsUseCase(colorsProvider = get()) }
    factory { CalcBpmByTapIntervalUseCase(bpmProvider = get()) }

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
            saveValuesUseCase = get(),
            restoreSavedValuesUseCase = get()
        )
    }
    viewModel {
        MainViewModel(
            playRotateClickUseCase = get(),
            setBpmUseCase = get(),
            observeBpmUseCase = get(),
            setTimeSignatureUseCase = get(),
            incrementBeatTypeUseCase = get(),
            observeBeatTypesUseCase = get(),
            startClickingUseCase = get(),
            stopClickingUseCase = get(),
            observeClickStateUseCase = get(),
            getClickerCallbackUseCase = get(),
            observeTrainingDataUseCase = get(),
            startTrainingUseCase = get(),
            stopTrainingUseCase = get(),
            observeTrainingStateUseCase = get(),
            observeColorsUseCase = get(),
            setColorsUseCase = get(),
            calcBpmByTapIntervalUseCase = get()
        )
    }
    viewModel { TrainingTypeSelectionViewModel(observeColorsUseCase = get()) }
    viewModel { TrainingSubtypeSelectionViewModel(observeColorsUseCase = get()) }
    viewModel {
        OptionsViewModel(
            clicker = get(),
            setTrainingDataUseCase = get(),
            getTrainingDataUseCase = get()
        )
    }
}