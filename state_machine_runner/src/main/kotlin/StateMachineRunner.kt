package com.gurunars.flashcards.state_machine_runner

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurunars.flashcards.state_machine.StateMachine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StateMachineRunner<State, Event>(
  private val stateMachine: StateMachine<State, Event>,
) : ViewModel(), DefaultLifecycleObserver {
  private val _events = MutableStateFlow<Event?>(null)
  private val _state = MutableStateFlow(stateMachine.initialState)

  val state: StateFlow<State> = _state
  val events: StateFlow<Event?> = _events

  fun send(event: Event) {
    viewModelScope.launch {
      _events.emit(event)
    }
  }

  override fun onCreate(owner: LifecycleOwner) {
    viewModelScope.launch {
      events.collect { event ->
        if (event != null) {
          process(event)
        }
      }
    }
  }

  private suspend fun process(event: Event) {
    var newState = nextState(event)
    while (newState != null) {
      _state.value = newState
      newState = nextState()
    }
  }

  private suspend fun nextState(event: Event? = null): State? =
    withContext(Dispatchers.Default) {
      try {
        stateMachine.nextState(_state.value, event)
      } catch (exception: Exception) {
        stateMachine.onError(_state.value, event, exception)
      }
    }
}
