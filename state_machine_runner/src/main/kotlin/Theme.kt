package com.gurunars.android_libs.state_machine_runner

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun Themed(isDark: Boolean? = null, content: @Composable () -> Unit) {
    val themePicker =
        if (isDark ?: isSystemInDarkTheme())
            ::dynamicDarkColorScheme
        else
            ::dynamicLightColorScheme
    MaterialTheme(
        colorScheme = themePicker(LocalContext.current),
        content = content
    )
}
