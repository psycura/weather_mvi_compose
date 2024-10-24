package com.example.weatherappmvi.domain.usecase

import com.example.weatherappmvi.domain.repository.FavoriteRepository
import org.koin.core.annotation.Factory

@Factory
class ObserveFavoriteStateUseCase(
    private val favoriteRepository: FavoriteRepository
) {
    operator fun invoke(cityId: Int) = favoriteRepository.observeIsFavorite(cityId)
}