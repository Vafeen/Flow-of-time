package ru.vafeen.presentation.common.components.bottom_bar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.vafeen.presentation.common.Screen

/**
 * Компонент нижней навигационной панели (BottomBar) для приложения на Jetpack Compose.
 *
 * Отображает навигационную панель с элементами, представленными в списке [screens].
 * Подсвечивает текущий выбранный экран [currentScreen].
 * При нажатии на элемент панели вызывает функцию [navigateTo] для перехода на соответствующий экран.
 *
 * @param containerColor Цвет фона панели навигации.
 * @param currentScreen Текущий активный экран, который подсвечивается на панели.
 * @param screens Список элементов панели навигации, каждый из которых содержит экран, иконку и описание.
 * @param navigateTo Функция обратного вызова для навигации на выбранный экран.
 */
@Composable
internal fun BottomBar(
    containerColor: Color,
    currentScreen: Screen,
    screens: List<BottomBarItem>,
    navigateTo: (Screen) -> Unit,
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        containerColor = containerColor
    ) {
        val size = screens.size
        screens.forEach { item ->
            NavigationBarItem(
                modifier = Modifier.weight(1f / size),
                selected = currentScreen == item.screen,
                enabled = currentScreen != item.screen,
                onClick = { navigateTo(item.screen) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    disabledIconColor = Color.Black,
                    unselectedIconColor = Color.Black.copy(alpha = 0.6f) // todo (custom theme)
                ),
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
