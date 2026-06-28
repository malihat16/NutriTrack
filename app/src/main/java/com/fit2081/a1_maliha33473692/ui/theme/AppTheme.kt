package com.fit2081.a1_maliha33473692.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun A1_maliha33473692Theme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme  = colors,
        typography    = Typography,
        content       = content
    )
}
