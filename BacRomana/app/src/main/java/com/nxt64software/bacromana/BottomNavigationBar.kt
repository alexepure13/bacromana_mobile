package com.nxt64software.bacromana

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Structură simplă pentru datele meniului
private data class NavItem(
    val screen: Screen,
    val icon: ImageVector,
    val labelResId: Int
)

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onSelect: (Screen) -> Unit
) {
    val isDark = isSystemInDarkTheme()

    // Culorile de bază ale barei
    val barContainerColor = if (isDark) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
    val barContentColor = if (isDark) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface

    // Culoarea cercului din spatele iconiței
    // Opacitate 0.15f pentru un efect subtil, dar vizibil
    val customSelectionColor = if (isDark) Color.White.copy(alpha = 0.15f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)

    // Culoarea textului și iconiței selectate
    val selectedTextCol = if (isDark) Color.White else MaterialTheme.colorScheme.primary

    val items = listOf(
        NavItem(Screen.Home, Icons.Filled.Home, R.string.home),
        NavItem(Screen.Sub1, Icons.Filled.Filter1, R.string.subiectul_i),
        NavItem(Screen.Sub2, Icons.Filled.Filter2, R.string.subiectul_ii),
        NavItem(Screen.Sub3, Icons.Filled.Filter3, R.string.subiectul_iii),
        NavItem(Screen.Quiz, Icons.Filled.HelpOutline, R.string.quiz)
    )

    NavigationBar(
        tonalElevation = 0.dp,
        containerColor = barContainerColor,
        contentColor = barContentColor
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.screen.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelect(item.screen) },
                icon = {
                    Box(
                        modifier = Modifier
                            // AM MĂRIT AICI DE LA 32.dp LA 48.dp
                            .size(48.dp)
                            .background(
                                color = if (isSelected) customSelectionColor else Color.Transparent,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = if (isSelected) selectedTextCol else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                label = {
                    Text(
                        text = stringResource(id = item.labelResId),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    // Important: Ascundem indicatorul default ca să se vadă doar Box-ul nostru
                    indicatorColor = Color.Transparent,

                    selectedTextColor = selectedTextCol,
                    selectedIconColor = selectedTextCol,

                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}