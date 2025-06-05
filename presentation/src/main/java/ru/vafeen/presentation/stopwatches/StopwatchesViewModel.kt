package ru.vafeen.presentation.stopwatches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.vafeen.domain.database.StopwatchRepository
import ru.vafeen.domain.utils.launchIO
import ru.vafeen.presentation.common.Screen
import ru.vafeen.presentation.navigation.NavRootIntent

@HiltViewModel(assistedFactory = StopwatchesViewModel.Factory::class)
internal class StopwatchesViewModel @AssistedInject constructor(
    private val stopwatchRepository: StopwatchRepository,
    @Assisted private val sendRootIntent: (NavRootIntent) -> Unit
) : ViewModel() {

    private val _state = MutableStateFlow(
        StopwatchesState(
            stopwatches = listOf(),
            timeNow = System.currentTimeMillis()
        )
    )
    val state = _state.asStateFlow()
    fun handleIntent(intent: StopWatchesIntent) {
        viewModelScope.launchIO {
            when (intent) {
                is StopWatchesIntent.NavigateTo -> navigateTo(id = intent.id)
            }
        }

    }

    init {
        viewModelScope.launchIO {
            stopwatchRepository.getAll().collect { stopwatches ->
                _state.update { it.copy(stopwatches = stopwatches) }
            }
        }
    }

    private fun navigateTo(id: Int) {
        sendRootIntent(NavRootIntent.NavigateToScreen(Screen.StopwatchData(id = id)))
    }

    @AssistedFactory
    interface Factory {
        fun create(sendRootIntent: (NavRootIntent) -> Unit): StopwatchesViewModel
    }
}