// Файл 5: TimersScreen.kt
package ru.vafeen.presentation.timers

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
 * Экран отображения списка таймеров.
 *
 * Отображает список таймеров с возможностью навигации к каждому из них,
 * а также предоставляет кнопку для добавления нового таймера.
 *
 * @param sendRootIntent Функция для отправки навигационных интентов в корневой навигатор.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun TimersScreen(sendRootIntent: (NavRootIntent) -> Unit) {
    val viewModel = hiltViewModel<TimersViewModel, TimersViewModel.Factory> { factory ->
        factory.create(sendRootIntent = sendRootIntent)
    }
    val state by viewModel.state.collectAsState()
    val isDeletingInProcess by remember {
        derivedStateOf {
            state.timersForDeleting.isNotEmpty()
        }
    }
    Scaffold(
        containerColor = Color.Transparent,
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                containerColor = AppTheme.colors.mainColor,
                onClick = { viewModel.handleIntent(TimersIntent.AddNew) },
                content = {
                    Icon(
                        painter = painterResource(R.drawable.add),
                        contentDescription = stringResource(R.string.add_timer)
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.handleIntent(TimersIntent.UndoDeleting) }) {
                        Icon(
                            painter = painterResource(R.drawable.close),
                            contentDescription = stringResource(R.string.undo),
                            tint = AppTheme.colors.text
                        )
                    }
                    TextForThisTheme(
                        text = "size ${state.timersForDeleting.size}",
                        fontSize = FontSize.huge27,
                    )
                    IconButton(onClick = { viewModel.handleIntent(TimersIntent.DeleteSelected) }) {
                        Icon(
                            painter = painterResource(R.drawable.delete),
                            contentDescription = stringResource(R.string.delete),
                            tint = AppTheme.colors.text
                        )
                    }
                }
            } else {
                TextForThisTheme(
                    text = stringResource(R.string.timers),
                    fontSize = FontSize.huge27,
                    modifier = Modifier.padding(vertical = 3.dp)
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 5.dp)
            ) {
                items(items = state.timers) { timer ->
                    TimerListItem(
                        modifier = Modifier.padding(vertical = 2.5.dp),
                        timer = timer,
                        timeNow = state.timeNow,
                        sendTimersIntent = viewModel::handleIntent,
                        isDeletingInProcess = isDeletingInProcess,
                        isItCandidateForDeleting = state.timersForDeleting
                            .containsKey(timer.id)
                    )
                }
            }
        }
    }
}
