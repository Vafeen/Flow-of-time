package ru.vafeen.presentation.stop_watch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vafeen.presentation.common.Screen
import ru.vafeen.presentation.navigation.NavRootIntent


@Composable
internal fun StopwatchDataScreen(
    sendRootIntent: (NavRootIntent) -> Unit,
    stopwatchData: Screen.StopwatchData
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("time ${stopwatchData.name}")
    }
}