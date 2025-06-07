package ru.vafeen.presentation.stopwatches

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vafeen.presentation.R
import ru.vafeen.presentation.common.components.TextForThisTheme
import ru.vafeen.presentation.navigation.NavRootIntent
import ru.vafeen.presentation.ui.theme.AppTheme
import ru.vafeen.presentation.ui.theme.FontSize

/**
 * Экран отображения списка секундомеров.
 *
 * Отображает список секундомеров с возможностью навигации к каждому из них,
 * а также предоставляет кнопку для добавления нового секундомера.
 *
 * @param sendRootIntent Функция для отправки навигационных интентов в корневой навигатор.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun StopwatchesScreen(sendRootIntent: (NavRootIntent) -> Unit) {
    val viewModel = hiltViewModel<StopwatchesViewModel, StopwatchesViewModel.Factory> { factory ->
        factory.create(sendRootIntent = sendRootIntent)
    }
    val state by viewModel.state.collectAsState()
    val isDeletingInProcess by remember {
        derivedStateOf {
            state.stopwatchesForDeleting.isNotEmpty()
        }
    }
    Scaffold(
        containerColor = Color.Transparent,
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                containerColor = AppTheme.colors.mainColor,
                onClick = { viewModel.handleIntent(StopwatchesIntent.AddNew) },
                content = {
                    Icon(
                        painter = painterResource(R.drawable.add),
                        contentDescription = stringResource(R.string.add_stopwatch)
                    )
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isDeletingInProcess) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextForThisTheme(text = "size ${state.stopwatchesForDeleting.size}")
                    Spacer(modifier = Modifier.width(20.dp))
                    IconButton(onClick = { viewModel.handleIntent(StopwatchesIntent.DeleteSelected) }) {
                        Icon(
                            painter = painterResource(R.drawable.delete),
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                }
            } else {
                TextForThisTheme(
                    text = stringResource(R.string.stopwatches),
                    fontSize = FontSize.huge27,
                    modifier = Modifier.padding(vertical = 3.dp)
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 5.dp)
            ) {
                items(items = state.stopwatches) { stopwatch ->
                    StopwatchListItem(
                        modifier = Modifier.padding(vertical = 2.5.dp),
                        stopwatch = stopwatch,
                        timeNow = state.timeNow,
                        sendStopwatchesIntent = viewModel::handleIntent,
                        isDeletingInProcess = isDeletingInProcess,
                        isItCandidateForDeleting = state.stopwatchesForDeleting
                            .containsKey(stopwatch.id)
                    )
                }
            }
        }
    }
}
