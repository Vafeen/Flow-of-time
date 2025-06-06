package ru.vafeen.presentation.stopwatches

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vafeen.presentation.navigation.NavRootIntent
import ru.vafeen.presentation.ui.theme.AppTheme

@Composable
internal fun StopwatchesScreen(sendRootIntent: (NavRootIntent) -> Unit) {
    val viewModel = hiltViewModel<StopwatchesViewModel, StopwatchesViewModel.Factory> { factory ->
        factory.create(sendRootIntent = sendRootIntent)
    }
    val state by viewModel.state.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(state.stopwatches) { stopwatch ->
            Column(
                modifier = Modifier
                    .border(border = BorderStroke(1.dp, AppTheme.colors.text))
                    .padding(3.dp)
            ) {
                Text("stop watch ${stopwatch.name}")
                Button(onClick = {
                    viewModel.handleIntent(StopWatchesIntent.NavigateTo(id = stopwatch.id))
                }) {
                    Text("navigate to stopwatch ${stopwatch.name}")
                }
            }
        }
    }

}