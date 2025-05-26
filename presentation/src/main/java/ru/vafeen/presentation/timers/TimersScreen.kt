package ru.vafeen.presentation.timers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vafeen.presentation.common.Screen
import ru.vafeen.presentation.navigation.NavRootIntent

@Composable
internal fun TimersScreen(sendRootIntent: (NavRootIntent) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("timers")
        Button(onClick = {
            sendRootIntent(NavRootIntent.NavigateToScreen(Screen.StopwatchData(name = "vafeen's timer")))
        }) {
            Text("navigate to some timer")
        }
    }
}