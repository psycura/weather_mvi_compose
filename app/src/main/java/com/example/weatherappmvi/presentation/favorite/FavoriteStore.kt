package com.example.weatherappmvi.presentation.favorite

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.weatherappmvi.presentation.favorite.FavoriteStore.Intent
import com.example.weatherappmvi.presentation.favorite.FavoriteStore.Label
import com.example.weatherappmvi.presentation.favorite.FavoriteStore.State

internal interface FavoriteStore : Store<Intent, State, Label> {

    sealed interface Intent {
    }

    data class State(
        val todo: Unit
    )

    sealed interface Label {
    }
}

internal class FavoriteStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): FavoriteStore =
        object : FavoriteStore, Store<Intent, State, Label> by storeFactory.create(
            name = "FavoriteStore",
            initialState = State(Unit),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            super.executeIntent(intent)
        }

        override fun executeAction(action: Action) {
            super.executeAction(action)
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State = State(Unit)

    }
}
