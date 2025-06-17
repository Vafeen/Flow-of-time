package ru.vafeen.presentation.timer_data

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vafeen.presentation.common.Screen
import ru.vafeen.presentation.navigation.NavRootIntent

@Composable
internal fun TimerDataScreen(
    sendRootIntent: (NavRootIntent) -> Unit,
    timerData: Screen.TimerData
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("time ${timerData.id}")
    }
}