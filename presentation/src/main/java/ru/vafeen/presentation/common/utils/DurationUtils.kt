package ru.vafeen.presentation.common.utils

import java.time.Duration

/**
 * Вычисляет разницу между двумя временными метками в миллисекундах.
 *
 * @receiver Более поздняя временная метка (в миллисекундах)
 * @param with Более ранняя временная метка (в миллисекундах)
 * @return Разница между метками в виде Duration
 */
internal fun Long.subtractDuration(with: Long): Duration = Duration.ofMillis(with - this)

/**
 * Преобразует Duration в строку формата "HH:mm:ss".
 *
 * Если длительность превышает 24 часа, часы могут быть больше 24.
 *
 * @receiver Объект Duration для форматирования
 * @return Строка с длительностью в формате "HH:mm:ss"
 */
internal fun Duration.toHHMMSS(): String {
    val totalSeconds = this.seconds
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d:%02d".format(hours, minutes, seconds)
}

/**
 * Преобразует миллисекунды в строку формата "HH:mm:ss".
 *
 * Если длительность превышает 24 часа, часы могут быть больше 24.
 *
 * @receiver Количество миллисекунд для форматирования.
 * @return Строка с длительностью в формате "HH:mm:ss".
 */
internal fun Long.toHHMMSS(): String {
    val totalSeconds = Duration.ofMillis(this).seconds
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d:%02d".format(hours, minutes, seconds)
}

/**
 * Округляет значение миллисекунд вниз до ближайшей полной секунды,
 * обнуляя миллисекунды.
 *
 * Пример: 5654 миллисекунд будет преобразовано в 5000 миллисекунд.
 *
 * @receiver Значение времени в миллисекундах.
 * @return Значение времени, округлённое вниз до целых секунд (в миллисекундах).
 */
internal fun Long.withoutMillis(): Long = this / 1000 * 1000