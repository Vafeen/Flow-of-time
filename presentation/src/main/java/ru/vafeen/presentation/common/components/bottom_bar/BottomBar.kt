package ru.vafeen.presentation.common.components.bottom_bar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vafeen.presentation.common.Screen

@Composable
internal fun BottomBar(
    currentScreen: Screen,
    screens: List<BottomBarItem>,
    navigateTo: (Screen) -> Unit,
) {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        val size = screens.size
        screens.forEach { item ->
            NavigationBarItem(
                modifier = Modifier.weight(1f / size),
                selected = currentScreen == item.screen,
                enabled = currentScreen != item.screen,
                onClick = { navigateTo(item.screen) },
                icon = {
                    Icon(
                        painter = item.icon,
                        contentDescription = item.contentDescription
                    )
                }
            )
        }
    }
}