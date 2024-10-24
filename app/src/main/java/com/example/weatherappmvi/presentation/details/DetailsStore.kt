package com.example.weatherappmvi.presentation.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.domain.entity.Forecast
import com.example.weatherappmvi.domain.usecase.ChangeFavoriteStateUseCase
import com.example.weatherappmvi.domain.usecase.GetForecastUseCase
import com.example.weatherappmvi.domain.usecase.ObserveFavoriteStateUseCase
import com.example.weatherappmvi.presentation.details.DetailsStore.Intent
import com.example.weatherappmvi.presentation.details.DetailsStore.Label
import com.example.weatherappmvi.presentation.details.DetailsStore.State
import kotlinx.coroutines.launch

interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object BackClick : Intent
        data object ChangeFavoriteStatusClick : Intent
    }

    data class State(
        val city: City,
        val isFavorite: Boolean,
        val forecastState: ForecastState
    ) {
        sealed interface ForecastState {
            data object Initial : ForecastState
            data object Loading : ForecastState
            data object Error : ForecastState
            data class Loaded(val forecast: Forecast) : ForecastState
        }
    }

    sealed interface Label {
        data object BackClick : Label
    }
}

class DetailsStoreFactory(
    private val storeFactory: StoreFactory,
    private val getForecastUseCase: GetForecastUseCase,
    private val changeFavoriteStateUseCase: ChangeFavoriteStateUseCase,
    private val observeFavoriteStateUseCase: ObserveFavoriteStateUseCase
) {

    fun create(city: City): DetailsStore =
        object : DetailsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "DetailsStore",
            initialState = State(
                city = city,
                isFavorite = false,
                forecastState = State.ForecastState.Initial
            ),
            bootstrapper = BootstrapperImpl(city),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class FavoriteStatusChanged(val isFavorite: Boolean) : Action
        data object ForecastIsLoading : Action
        data class ForecastLoaded(val forecast: Forecast) : Action
        data object ForecastLoadingError : Action
    }

    private sealed interface Msg {
        data class FavoriteStatusChanged(val isFavorite: Boolean) : Msg
        data object ForecastIsLoading : Msg
        data class ForecastLoaded(val forecast: Forecast) : Msg
        data object ForecastLoadingError : Msg
    }

    private inner class BootstrapperImpl(private val city: City) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                observeFavoriteStateUseCase(city.id).collect { isFavorite ->
                    dispatch(Action.FavoriteStatusChanged(isFavorite))
                }
            }

            scope.launch {
                dispatch(Action.ForecastIsLoading)
                try {
                    val forecast = getForecastUseCase(city.id)
                    dispatch(Action.ForecastLoaded(forecast))
                } catch (e: Exception) {
                    dispatch(Action.ForecastLoadingError)
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                Intent.BackClick -> {
                    publish(Label.BackClick)
                }

                Intent.ChangeFavoriteStatusClick -> {
                    val state = state()
                    scope.launch {
                        if (state.isFavorite) {
                            changeFavoriteStateUseCase.removeFromFavorite(state.city.id)
                        } else {
                            changeFavoriteStateUseCase.addToFavorite(state.city)
                        }
                    }
                }
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.FavoriteStatusChanged -> {
                    dispatch(Msg.FavoriteStatusChanged(action.isFavorite))
                }

                is Action.ForecastIsLoading -> {
                    dispatch(Msg.ForecastIsLoading)
                }

                is Action.ForecastLoaded -> {
                    dispatch(Msg.ForecastLoaded(action.forecast))
                }

                Action.ForecastLoadingError -> {
                    dispatch(Msg.ForecastLoadingError)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.FavoriteStatusChanged -> {
                copy(isFavorite = msg.isFavorite)
            }

            is Msg.ForecastLoaded -> {
                copy(forecastState = State.ForecastState.Loaded(msg.forecast))
            }

            Msg.ForecastIsLoading -> {
                copy(forecastState = State.ForecastState.Loading)
            }


            Msg.ForecastLoadingError -> {
                copy(forecastState = State.ForecastState.Error)
            }

        }
    }
}
