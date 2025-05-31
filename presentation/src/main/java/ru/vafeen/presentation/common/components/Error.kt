package ru.vafeen.presentation.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Компонент для отображения сообщения об ошибке по центру экрана.
 *
 * @param error Текст сообщения об ошибке
 */
@Composable
fun Error(error: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        TextForThisTheme(text = error)
    }
}
