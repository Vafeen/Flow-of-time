package ru.vafeen.presentation.new_stopwatch

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vafeen.presentation.R
import ru.vafeen.presentation.common.components.StopwatchComponent
import ru.vafeen.presentation.common.components.TextForThisTheme
import ru.vafeen.presentation.navigation.NavRootIntent

@Composable
internal fun NewStopwatchDataScreen(sendRootIntent: (NavRootIntent) -> Unit) {
    val viewModel: NewStopwatchDataViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    StopwatchComponent(
        stopwatch = state.stopwatch,
        fab = {
            state.stopwatch.let { stopwatch ->
                if (state.isAddedToDb) {
                    FloatingActionButton(
                        onClick = { viewModel.handleIntent(NewStopwatchDataIntent.ChangeState) }
                    ) {
                        Icon(
                            painter = painterResource(
                                if (stopwatch.stopTime != null) R.drawable.resume else R.drawable.pause
                            ), contentDescription = stringResource(
                                if (stopwatch.stopTime != null) R.string.resume else R.string.pause
                            )
                        )
                    }
                } else {
                    ExtendedFloatingActionButton(onClick = {
                        viewModel.handleIntent(NewStopwatchDataIntent.AddAndStart)
                    }) {
                        TextForThisTheme("Add to db")
                        Icon(
                            painter = painterResource(
                                if (stopwatch.stopTime != null) R.drawable.resume else R.drawable.pause
                            ), contentDescription = stringResource(
                                if (stopwatch.stopTime != null) R.string.resume else R.string.pause
                            )
                        )
                    }
                }
            }
        },
        timeNow = state.timeNow,
    )
}