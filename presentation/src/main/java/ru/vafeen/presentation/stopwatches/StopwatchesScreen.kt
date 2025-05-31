package ru.vafeen.presentation.stopwatches

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vafeen.presentation.common.Screen
import ru.vafeen.presentation.navigation.NavRootIntent

@Composable
internal fun StopwatchesScreen(sendRootIntent: (NavRootIntent) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("stop watchers")
        Button(onClick = { sendRootIntent(NavRootIntent.NavigateToScreen(Screen.StopwatchData(id = 1))) }) {
            Text("navigate to some stopwatch")
        }
    }
}