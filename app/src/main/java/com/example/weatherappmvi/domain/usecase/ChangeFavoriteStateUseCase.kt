package com.example.weatherappmvi.domain.usecase

import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.domain.repository.FavoriteRepository
import org.koin.core.annotation.Factory

@Factory
class ChangeFavoriteStateUseCase(
    private val favoriteRepository: FavoriteRepository
) {
    suspend fun addToFavorite(city: City) = favoriteRepository.addToFavorite(city)

    suspend fun removeFromFavorite(cityId: Int) = favoriteRepository.removeFromFavorite(cityId)
}