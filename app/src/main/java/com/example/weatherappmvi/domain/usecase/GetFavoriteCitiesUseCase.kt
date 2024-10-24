package com.example.weatherappmvi.domain.usecase

import com.example.weatherappmvi.domain.repository.FavoriteRepository
import org.koin.core.annotation.Factory

@Factory
class GetFavoriteCitiesUseCase(
    private val favoriteRepository: FavoriteRepository
) {
    operator fun invoke() = favoriteRepository.favoriteCities
}