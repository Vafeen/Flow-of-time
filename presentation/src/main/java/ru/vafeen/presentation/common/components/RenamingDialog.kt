package ru.vafeen.presentation.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.vafeen.presentation.R
import ru.vafeen.presentation.common.components.button.DialogButton
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * Диалог для переименования с полем ввода и кнопками подтверждения и отмены.
 *
 * @param value Текущее значение текста в поле ввода.
 * @param onDismissRequest Лямбда для запуска процесса закрытия диалога.
 * @param onSave Лямбда для запуска процесса сохранения нового имени.
 */
@Composable
internal fun RenamingDialog(value: String, onDismissRequest: () -> Unit, onSave: (String) -> Unit) {
    var text by remember { mutableStateOf(value) }
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.background),
            border = BorderStroke(width = 2.dp, color = AppTheme.colors.text)
        ) {
            Column(modifier = Modifier.padding(15.dp)) {
                OutlinedTextField(
                    placeholder = { Text(text = "name", color = Color.Gray) },
                    value = text, onValueChange = { text = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = AppTheme.colors.background,
                        unfocusedContainerColor = AppTheme.colors.background,
                        focusedTextColor = AppTheme.colors.text,
                        unfocusedTextColor = AppTheme.colors.text,

                        )
                )
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DialogButton(
                        onClick = onDismissRequest,
                        containerColor = AppTheme.colors.error
                    ) {
                        TextForThisTheme(text = stringResource(R.string.undo))
                    }
                    DialogButton(
                        enabled = text != value,
                        onClick = {
                            onSave(text)
                            onDismissRequest()
                        }, containerColor = AppTheme.colors.mainColor // TODO (custom color)
                    ) {
                        TextForThisTheme(text = stringResource(R.string.apply))
                    }
                }
            }
        }
    }
}
