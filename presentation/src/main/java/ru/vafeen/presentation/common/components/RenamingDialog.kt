package ru.vafeen.presentation.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog

/**
 * Диалог для переименования с полем ввода и кнопками подтверждения или отмены.
 *
 * @param value Текущее значение текста в поле ввода.
 * @param onDismissRequest Лямбда для запуска процесса закрытия диалога.
 * @param onSave Лямбда для запуска процесса сохранения нового имени.
 */
@Composable
internal fun RenamingDialog(value: String, onDismissRequest: () -> Unit, onSave: (String) -> Unit) {
    var text by remember { mutableStateOf(value) }
    Dialog(onDismissRequest = onDismissRequest) {
        Column {
            TextField(value = text, onValueChange = { text = it })
            Row {
                Button(onClick = onDismissRequest) {
                    TextForThisTheme(text = "cancel")
                }
                Button(onClick = {
                    onSave(text)
                    onDismissRequest()
                }) {
                    TextForThisTheme(text = "apply")
                }
            }
        }
    }
}
