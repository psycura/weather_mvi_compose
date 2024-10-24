package com.example.weatherappmvi.presentation.extensions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.math.roundToInt

fun ComponentContext.componentScope(): CoroutineScope =
    CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
        .apply {
            lifecycle.doOnDestroy { cancel() }
        }

fun Float.tempToFormatedString(): String = "${roundToInt()}°C"