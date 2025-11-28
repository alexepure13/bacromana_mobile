package com.nxt64software.bacromana

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onSelect: (Screen) -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val background = if (isDark) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
    val contentColor = if (isDark) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface

    // fundal pentru iconița selectată
    val selectedIconBackground = if (isDark) Color.White.copy(alpha = 0.1f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)

    NavigationBar(
        tonalElevation = 0.dp,
        containerColor = background,
        contentColor = contentColor
    ) {
        // Home
        NavigationBarItem(
            selected = currentRoute == Screen.Home.route,
            onClick = { onSelect(Screen.Home) },
            icon = {
                Box(
                    modifier = androidx.compose.ui.Modifier
                        .size(32.dp)
                        .background(
                            color = if (currentRoute == Screen.Home.route) selectedIconBackground else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            label = { Text(stringResource(id = R.string.home)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = if (isDark) Color.White else MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selectedTextColor = if (isDark) Color.White else MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        // Subiectul I
        NavigationBarItem(
            selected = currentRoute == Screen.Sub1.route,
            onClick = { onSelect(Screen.Sub1) },
            icon = {
                Box(
                    modifier = androidx.compose.ui.Modifier
                        .size(32.dp)
                        .background(
                            color = if (currentRoute == Screen.Sub1.route) selectedIconBackground else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Filter1,
                        contentDescription = null,
                        tint = contentColor
                    )
                }
            },
            label = { Text(stringResource(id = R.string.subiectul_i)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = if (isDark) Color.White else MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selectedTextColor = if (isDark) Color.White else MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        // Subiectul II
        NavigationBarItem(
            selected = currentRoute == Screen.Sub2.route,
            onClick = { onSelect(Screen.Sub2) },
            icon = {
                Box(
                    modifier = androidx.compose.ui.Modifier
                        .size(32.dp)
                        .background(
                            color = if (currentRoute == Screen.Sub2.route) selectedIconBackground else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Filter2,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            label = { Text(stringResource(id = R.string.subiectul_ii)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = if (isDark) Color.White else MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selectedTextColor = if (isDark) Color.White else MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        // Subiectul III
        NavigationBarItem(
            selected = currentRoute == Screen.Sub3.route,
            onClick = { onSelect(Screen.Sub3) },
            icon = {
                Box(
                    modifier = androidx.compose.ui.Modifier
                        .size(32.dp)
                        .background(
                            color = if (currentRoute == Screen.Sub3.route) selectedIconBackground else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Filter3,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            label = { Text(stringResource(id = R.string.subiectul_iii)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = if (isDark) Color.White else MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selectedTextColor = if (isDark) Color.White else MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        // Quiz
        NavigationBarItem(
            selected = currentRoute == Screen.Quiz.route,
            onClick = { onSelect(Screen.Quiz) },
            icon = {
                Box(
                    modifier = androidx.compose.ui.Modifier
                        .size(32.dp)
                        .background(
                            color = if (currentRoute == Screen.Quiz.route) selectedIconBackground else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.HelpOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            label = { Text(stringResource(id = R.string.quiz)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = if (isDark) Color.White else MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selectedTextColor = if (isDark) Color.White else MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}
