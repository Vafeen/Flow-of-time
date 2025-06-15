package ru.vafeen.presentation.common.components.button

import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * Компонент кнопки с иконкой, стилизованный под тему приложения.
 *
 * @param modifier Модификатор для настройки внешнего вида и поведения кнопки.
 * @param onClick Лямбда, вызываемая при нажатии на кнопку.
 * @param icon Композиция, отображающая иконку внутри кнопки.
 */
@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        content = icon,
        colors = IconButtonDefaults.iconButtonColors(containerColor = AppTheme.colors.mainColor)
    )
}
