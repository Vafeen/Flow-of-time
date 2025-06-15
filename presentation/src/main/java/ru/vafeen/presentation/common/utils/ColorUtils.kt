package ru.vafeen.presentation.common.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

/**
 * Возвращает подходящий цвет (белый или черный) для текста или элементов UI,
 * чтобы обеспечить хорошую читаемость на фоне данного цвета.
 *
 * @receiver [Color] цвет фона.
 * @return [Color] белый, если цвет темный, иначе черный.
 */
fun Color.suitableColor(): Color =
    if (this.isDark()) Color.White else Color.Black

/**
 * Определяет, является ли цвет темным на основе его яркости.
 *
 * @receiver [Color] проверяемый цвет.
 * @return `true`, если яркость цвета меньше 0.4, иначе `false`.
 */
fun Color.isDark(): Boolean = ColorUtils.calculateLuminance(this.toArgb()) < 0.4
