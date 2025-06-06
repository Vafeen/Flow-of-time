package ru.vafeen.presentation.stopwatches

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vafeen.presentation.R
import ru.vafeen.presentation.navigation.NavRootIntent
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * Экран отображения списка секундомеров.
 *
 * Отображает список секундомеров с возможностью навигации к каждому из них,
 * а также предоставляет кнопку для добавления нового секундомера.
 *
 * @param sendRootIntent Функция для отправки навигационных интентов в корневой навигатор.
 */
@Composable
internal fun StopwatchesScreen(sendRootIntent: (NavRootIntent) -> Unit) {
    val viewModel = hiltViewModel<StopwatchesViewModel, StopwatchesViewModel.Factory> { factory ->
        factory.create(sendRootIntent = sendRootIntent)
    }
    val state by viewModel.state.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.handleIntent(StopWatchesIntent.AddNew) },
                content = {
                    Icon(
                        painter = painterResource(R.drawable.add),
                        contentDescription = stringResource(R.string.add_stopwatch)
                    )
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(items = state.stopwatches) { stopwatch ->
                Column(
                    modifier = Modifier
                        .border(border = BorderStroke(1.dp, AppTheme.colors.text))
                        .padding(3.dp)
                ) {
                    Text(text = "Stopwatch ${stopwatch.name}")
                    Button(onClick = {
                        viewModel.handleIntent(StopWatchesIntent.NavigateTo(id = stopwatch.id))
                    }) {
                        Text(text = "Navigate to stopwatch ${stopwatch.name}")
                    }
                }
            }
        }
    }
}
