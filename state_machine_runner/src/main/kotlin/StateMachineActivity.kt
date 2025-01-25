package com.gurunars.android_libs.state_machine_runner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.activity.addCallback
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.gurunars.android_libs.state_machine.StateMachine
import kotlinx.coroutines.launch

abstract class StateMachineActivity<State, Event>: ComponentActivity() {

    abstract val stateMachine: StateMachine<State, Event>

    @Composable
    abstract fun Content(state: State, emit: (Event) -> Unit)

    private val runner: StateMachineRunner<State, Event> by viewModels {
        StateMachineRunnerFactory(stateMachine)
    }

    /** Handles side effect on activity level */
    protected fun onEvent(event: Event) {

    }

    fun emit(event: Event) {
        runner.emit(event)
    }

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                runner.events.collect { event ->
                    if (event != null) {
                        onEvent(event)
                    }
                }
            }
        }

        actionBar?.hide()
        setContent {
            val state by runner.state.collectAsState()
            if (state == stateMachine.endState) {
                finish()
            }
            Themed {
                Content(state, runner::emit)
            }
        }

        runner.init()

        onBackPressedDispatcher.addCallback {
            runner.emit(stateMachine.backEvent)
        }
    }

}
