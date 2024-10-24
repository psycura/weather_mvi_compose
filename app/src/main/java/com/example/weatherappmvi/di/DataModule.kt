package com.example.weatherappmvi.di

import com.example.weatherappmvi.data.local.db.FavoriteCitiesDao
import com.example.weatherappmvi.data.local.db.FavoriteDatabase
import com.example.weatherappmvi.data.local.db.provideFavoriteCitiesDao
import org.koin.dsl.module

val dataModule = module {
    single<FavoriteDatabase> { FavoriteDatabase.getInstance(context = get()) }
    single<FavoriteCitiesDao> { provideFavoriteCitiesDao(favoriteDatabase = get()) }
}