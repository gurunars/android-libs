package com.gurunars.android_libs.state_machine_runner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.activity.addCallback
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.gurunars.android_libs.state_machine.StateMachine

abstract class StateMachineActivity<State, Event>: ComponentActivity() {

    abstract val stateMachine: StateMachine<State, Event>

    @Composable
    abstract fun Content(state: State, emit: (Event) -> Unit)

    private val runner: StateMachineRunner<State, Event> by viewModels {
        StateMachineRunnerFactory(stateMachine)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
