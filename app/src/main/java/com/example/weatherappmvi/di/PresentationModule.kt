package com.example.weatherappmvi.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.weatherappmvi.presentation.details.DetailsComponent
import com.example.weatherappmvi.presentation.details.DetailsComponentImpl
import com.example.weatherappmvi.presentation.details.DetailsStoreFactory
import com.example.weatherappmvi.presentation.favorite.FavoriteComponent
import com.example.weatherappmvi.presentation.favorite.FavoriteComponentImpl
import com.example.weatherappmvi.presentation.favorite.FavoriteStoreFactory
import com.example.weatherappmvi.presentation.root.RootComponent
import com.example.weatherappmvi.presentation.root.RootComponentImpl
import com.example.weatherappmvi.presentation.search.SearchComponent
import com.example.weatherappmvi.presentation.search.SearchComponentImpl
import com.example.weatherappmvi.presentation.search.SearchStoreFactory
import org.koin.dsl.module

val presentationModule = module {
    single<DetailsStoreFactory> { DetailsStoreFactory(DefaultStoreFactory(), get(), get(), get()) }
    single<FavoriteStoreFactory> { FavoriteStoreFactory(DefaultStoreFactory(), get(), get()) }
    single<SearchStoreFactory> { SearchStoreFactory(DefaultStoreFactory(), get(), get()) }

    single<SearchComponent.Factory> { SearchComponentImpl.Factory(get()) }
    single<DetailsComponent.Factory> { DetailsComponentImpl.Factory(get()) }
    single<FavoriteComponent.Factory> { FavoriteComponentImpl.Factory(get()) }
    single<RootComponent.Factory> { RootComponentImpl.Factory(get(), get(), get()) }
}