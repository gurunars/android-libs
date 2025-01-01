package com.gurunars.android_libs.state_machine_runner

import com.gurunars.android_libs.state_machine.StateMachine
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.test.runTest

sealed interface State

sealed interface Event

data object InitialState: State

data object TargetState: State

data object ErrorState: State

data object EndState: State

data object TestEvent: Event

data object ErrorEvent: Event

data object BackEvent: Event

class TestStateMachine: StateMachine<State, Event> {
    override val initialState = InitialState
    override val backEvent = BackEvent
    override val endState = EndState

    override suspend fun onError(currentState: State, event: Event?, exception: Exception): State =
        ErrorState

    override suspend fun nextState(currentState: State, event: Event?): State =
        when(event) {
            ErrorEvent -> {
                throw Exception("Boom!!!")
            }
            TestEvent -> TargetState
            BackEvent -> EndState
            else -> currentState
        }
}

private suspend fun waitUntil(
    stepMs: Long=30,
    maxWait: Long=1000,
    condition: () -> Boolean
) {
    val start = System.currentTimeMillis()
    while (!condition()) {
        if (System.currentTimeMillis() - start > maxWait) {
            throw Exception("Timed out waiting for condition")
        }
        delay(stepMs)
    }
}

private fun<T> StateFlow<T>.expectState(
    expectedState: T,
    stepMs: Long=30,
    maxWait: Long=1000,
) = runTest {
    val results = mutableListOf<T>()

    val job = launch {
       toList(results)
    }

    waitUntil(stepMs, maxWait) { results.lastOrNull() == expectedState }

    job.cancel()
}

class StateMachineRunnerTest {

    private lateinit var model: StateMachineRunner<State, Event>

    @Before
    fun setUp() {
        model = StateMachineRunner(
            TestStateMachine()
        )
        model.init()
    }

    @Test
    fun `Sets the initial state upon initialization`() {
        model.state.expectState(InitialState)
    }


    @Test
    fun `Event causes transition to a target state`() = runTest {
        model.emit(TestEvent)
        model.state.expectState(TargetState)
    }

    @Test
    fun `Error during transition causes transition to error state`() = runTest {
        model.emit(ErrorEvent)
        model.state.expectState(ErrorState)
    }

}