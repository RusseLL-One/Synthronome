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
import com.one.russell.metroman_20.domain.wrappers.Flasher
import com.one.russell.metroman_20.domain.wrappers.Vibrator
import com.one.russell.metroman_20.presentation.screens.bookmarks.BookmarksViewModel
import com.one.russell.metroman_20.presentation.screens.main.MainViewModel
import com.one.russell.metroman_20.presentation.screens.settings.SettingsViewModel
import com.one.russell.metroman_20.presentation.screens.training.training_subtype_selection.TrainingSubtypeSelectionViewModel
import com.one.russell.metroman_20.presentation.screens.training.options.OptionsViewModel
import com.one.russell.metroman_20.presentation.screens.training.training_type_selection.TrainingTypeSelectionViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

fun appModule() = module {

    single { ClickerCallback() }
    single { Vibrator(context = androidContext()) }
    single { Flasher(context = androidContext()) }
    single { Clicker(callback = get(), vibrator = get(), flasher = get(), optionsProvider = get(), beatTypesProvider = get(), bpmProvider = get(), androidContext().assets) }
    single { TrainingProcessor(clicker = get(), bpmProvider = get(), beatTypesProvider = get(), trainingStateProvider = get()) }
    single { PreferencesDataStore(androidContext()) }

    // providers
    single { ClickStateProvider() }
    single { TrainingStateProvider() }
    single { BeatTypesProvider() }
    single { BpmProvider() }
    single { ColorsProvider() }
    single { OptionsProvider() }
    single { BookmarksProvider() }

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
    factory {
        RestoreSavedValuesUseCase(
            bpmProvider = get(),
            beatTypesProvider = get(),
            colorsProvider = get(),
            optionsProvider = get(),
            bookmarksProvider = get(),
            dataStore = get()
        )
    }
    factory {
        SaveValuesUseCase(
            bpmProvider = get(),
            beatTypesProvider = get(),
            colorsProvider = get(),
            optionsProvider = get(),
            bookmarksProvider = get(),
            dataStore = get()
        )
    }
    factory { ObserveBpmUseCase(bpmProvider = get()) }
    factory { StartClickingUseCase(androidContext()) }
    factory { StopClickingUseCase(androidContext()) }
    factory { ObserveClickStateUseCase(clickStateProvider = get()) }
    factory { ObserveClickUseCase(clicker = get()) }
    factory { PlayRotateClickUseCase(clicker = get()) }
    factory { SetBpmUseCase(bpmProvider = get()) }
    factory { SetTimeSignatureUseCase(beatTypesProvider = get()) }
    factory { IncrementBeatTypeUseCase(beatTypesProvider = get()) }
    factory { ObserveBeatTypesUseCase(beatTypesProvider = get()) }
    factory { ObserveColorsUseCase(colorsProvider = get()) }
    factory { SetColorsUseCase(colorsProvider = get()) }
    factory { CalcBpmByTapIntervalUseCase(bpmProvider = get()) }
    factory { SetSoundPresetUseCase(optionsProvider = get()) }
    factory { SetVibrationEnabledUseCase(dataStore = get()) }
    factory { SetFlashEnabledUseCase(optionsProvider = get()) }
    factory { GetCurrentOptionsUseCase(optionsProvider = get()) }
    factory { CheckIfFlashAvailableUseCase(flasher = get()) }

    // bookmarks use cases
    factory { AddBookmarkUseCase(bpmProvider = get(), beatTypesProvider = get(), bookmarksProvider = get()) }
    factory { ObserveBookmarksUseCase(bookmarksProvider = get()) }
    factory { RemoveBookmarkUseCase(bookmarksProvider = get()) }
    factory { ToggleBookmarkSelectionUseCase(bookmarksProvider = get()) }
    factory { ResetBookmarksSelectionUseCase(bookmarksProvider = get()) }
    factory { ClearAllBookmarksUseCase(bookmarksProvider = get()) }
    factory { IsAnyBookmarkSelectedUseCase(bookmarksProvider = get()) }

    // training use cases
    factory { StartTrainingUseCase(trainingProcessor = get(), startClickingUseCase = get()) }
    factory { StopTrainingUseCase(trainingProcessor = get()) }
    factory { SaveTrainingDataUseCase(dataStore = get()) }
    factory { RestoreTrainingDataUseCase(dataStore = get()) }
    factory { ObserveTrainingStateUseCase(trainingStateProvider = get()) }

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
            observeClickUseCase = get(),
            stopTrainingUseCase = get(),
            observeTrainingStateUseCase = get(),
            observeColorsUseCase = get(),
            calcBpmByTapIntervalUseCase = get()
        )
    }
    viewModel {
        SettingsViewModel(
            setSoundPresetUseCase = get(),
            observeColorsUseCase = get(),
            setColorsUseCase = get(),
            setVibrationEnabledUseCase = get(),
            setFlashEnabledUseCase = get(),
            getCurrentOptionsUseCase = get(),
            checkIfFlashAvailableUseCase = get()
        )
    }
    viewModel {
        BookmarksViewModel(
            observeColorsUseCase = get(),
            observeBookmarksUseCase = get(),
            removeBookmarkUseCase = get(),
            toggleBookmarkSelectionUseCase = get(),
            clearAllBookmarksUseCase = get(),
        )
    }
    viewModel { TrainingTypeSelectionViewModel(observeColorsUseCase = get()) }
    viewModel { TrainingSubtypeSelectionViewModel(observeColorsUseCase = get()) }
    viewModel {
        OptionsViewModel(
            playRotateClickUseCase = get(),
            saveTrainingDataUseCase = get(),
            restoreTrainingDataUseCase = get(),
            startTrainingUseCase = get(),
            observeColorsUseCase = get()
        )
    }
}