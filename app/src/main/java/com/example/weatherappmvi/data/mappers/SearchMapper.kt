package com.example.weatherappmvi.data.mappers

import com.example.weatherappmvi.data.model.CityDto
import com.example.weatherappmvi.domain.entity.City

fun CityDto.toEntity(): City = City(id = id, name = name, country = country)

fun List<CityDto>.toEntities(): List<City> = map { it.toEntity() }