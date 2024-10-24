package com.example.weatherappmvi.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.example.weatherappmvi.presentation.root.RootComponent
import com.example.weatherappmvi.presentation.root.RootContent
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootComponentFactory: RootComponent.Factory by inject()
        val rootComponent = rootComponentFactory(defaultComponentContext())

//        enableEdgeToEdge()

        setContent {
            KoinAndroidContext() {
                RootContent(component = rootComponent)
            }
        }
    }
}
