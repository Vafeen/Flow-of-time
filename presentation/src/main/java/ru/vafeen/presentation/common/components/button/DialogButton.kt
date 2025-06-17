package ru.vafeen.presentation.common.components.button

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.vafeen.presentation.common.utils.suitableColor

/**
 * Кнопка с настраиваемыми параметрами для использования в диалогах и других компонентах.
 *
 * @param onClick Лямбда для запуска процесса обработки нажатия на кнопку.
 * @param modifier Модификатор для настройки внешнего вида и поведения кнопки.
 * @param enabled Флаг, указывающий на процесс включения или отключения кнопки.
 * @param contentPadding Отступы внутри кнопки.
 * @param content Содержимое кнопки — композируемый лямбда-блок с областью RowScope.
 */
@Composable
fun DialogButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = containerColor.suitableColor()
        ),
        content = content
    )
}
