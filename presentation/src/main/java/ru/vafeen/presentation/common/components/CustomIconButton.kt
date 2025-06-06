package ru.vafeen.presentation.common.components

import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import ru.vafeen.presentation.ui.theme.AppTheme

@Composable
fun CustomIconButton(onClick: () -> Unit, icon: @Composable () -> Unit) {
    IconButton(
        onClick = onClick,
        content = icon,
        colors = IconButtonDefaults.iconButtonColors(containerColor = AppTheme.colors.mainColor)
    )
}