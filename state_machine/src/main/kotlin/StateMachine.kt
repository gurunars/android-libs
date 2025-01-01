package com.gurunars.android_libs.state_machine

interface StateMachine<State, Event> {
  val initialState: State
  val backEvent: Event
  val endState: State
  suspend fun nextState(currentState: State, event: Event?): State
  suspend fun onError(currentState: State,  event: Event?, exception: Exception): State
}
