package ru.vafeen.presentation.common.components

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * Компонент для отображения текста с использованием темы приложения.
 *
 * @param text Текст для отображения.
 * @param modifier Модификатор для настройки внешнего вида и поведения компонента.
 * @param fontSize Размер шрифта текста.
 * @param textAlign Выравнивание текста.
 * @param style Стиль текста.
 * @param overflow Поведение при переполнении текста.
 * @param maxLines Максимальное количество строк для отображения текста.
 * @param textDecoration Оформление текста (например, перечеркивание).
 */
@Composable
internal fun TextForThisTheme(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
    style: TextStyle = LocalTextStyle.current,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = 1,
    textDecoration: TextDecoration = TextDecoration.None, // Устанавливаем перечеркивание текста
) {
    Text(
        text = text,
        modifier = modifier,
        color = AppTheme.colors.text,
        fontSize = fontSize,
        textAlign = textAlign,
        style = style,
        overflow = overflow,
        maxLines = maxLines,
        textDecoration = textDecoration
    )
}
