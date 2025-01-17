package com.gurunars.android_libs.state_machine_runner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gurunars.android_libs.state_machine.StateMachine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StateMachineRunnerFactory<State, Event>(
  private val stateMachine: StateMachine<State, Event>
) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  @Override
  override fun <T : ViewModel> create(modelClass: Class<T>): T =
    StateMachineRunner(stateMachine) as T

}

class StateMachineRunner<State, Event>(
  private val stateMachine: StateMachine<State, Event>,
): ViewModel() {
  private val _events = MutableStateFlow<Event?>(null)
  private val _state = MutableStateFlow(stateMachine.initialState)
  private var initialized = false

  val state: StateFlow<State> = _state

  fun emit(event: Event) {
    viewModelScope.launch {
      _events.emit(event)
    }
  }

  fun init() {
    if (initialized) {
      return
    }
    initialized = true
    viewModelScope.launch {
      _events.collect { event ->
        if (event != null) {
          process(event)
          // To dedup the event sequence
          _events.emit(null)
        }
      }
    }
    viewModelScope.launch {
      process(null)
    }
  }

  private suspend fun process(event: Event?) {
    var newState = nextState(event)
    while (newState != _state.value) {
      _state.emit(newState)
      newState = nextState()
    }
  }

  private suspend fun nextState(event: Event? = null): State =
    withContext(Dispatchers.Default) {
      try {
        stateMachine.nextState(_state.value, event)
      } catch (exception: Exception) {
        stateMachine.onError(_state.value, event, exception)
      }
    }
}
