package ru.vafeen.domain.domain_models

/**
 * Модель доменного слоя, представляющая таймер с установленной длительностью и текущим состоянием.
 *
 * @property id Уникальный идентификатор таймера.
 * @property name Название или описание таймера.
 * @property initialDurationMillis Изначально установленная длительность таймера в миллисекундах.
 * @property remainingTimeMillis Текущее оставшееся время таймера в миллисекундах.
 * @property isRunning Флаг, указывающий на процесс работы таймера (true — таймер запущен).
 * @property startTime Временная метка запуска таймера в миллисекундах с эпохи, или null если таймер не запущен.
 */
data class Timer(
    val id: Long = 0L,
    val name: String = "Timer ${System.currentTimeMillis()}",
    val initialDurationMillis: Long = 0L,
    val remainingTimeMillis: Long = 0L,
    val isRunning: Boolean = false,
    val startTime: Long? = null,
) {
    companion object {
        /**
         * Создает новый экземпляр таймера с нулевой длительностью и остановленным состоянием.
         * Время отображается как 00:00:00.
         *
         * @return Новый таймер с начальными значениями.
         */
        fun newInstance(): Timer = Timer()
    }
}
