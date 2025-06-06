package ru.vafeen.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import ru.vafeen.presentation.navigation.NavRoot
import ru.vafeen.presentation.ui.theme.MainTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainTheme {
                NavRoot()
            }
        }
    }

    companion object {
        val x = 6
    }

    object X {
        val x = 5
    }

}