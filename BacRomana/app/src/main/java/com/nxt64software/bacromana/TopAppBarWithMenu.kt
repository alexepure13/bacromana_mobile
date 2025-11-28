package com.nxt64software.bacromana

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithMenu(
    navigateTo: (String) -> Unit,
    onReload: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val isDark = isSystemInDarkTheme()
    val background = if (isDark) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
    val contentColor = if (isDark) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface

    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onReload) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reîncarcă",
                    tint = contentColor
                )
            }
        },
        title = { Text("Bacalaureat 2025", color = contentColor) },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Meniu", tint = contentColor)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.home)) },
                    onClick = {
                        navigateTo(Screen.Home.route); expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.subiectul_i_long)) },
                    onClick = {
                        navigateTo(Screen.Sub1.route); expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.subiectul_ii_long)) },
                    onClick = {
                        navigateTo(Screen.Sub2.route); expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.subiectul_iii_long)) },
                    onClick = {
                        navigateTo(Screen.Sub3.route); expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.quiz)) },
                    onClick = {
                        navigateTo(Screen.Quiz.route); expanded = false
                    }
                )
                Divider()
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.despre)) },
                    onClick = {
                        navigateTo(Screen.About.route); expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.politica)) },
                    onClick = {
                        navigateTo(Screen.Privacy.route); expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.termeni)) },
                    onClick = {
                        navigateTo(Screen.Terms.route); expanded = false
                    }
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = background,
            titleContentColor = contentColor,
            navigationIconContentColor = contentColor,
            actionIconContentColor = contentColor
        )
    )
}
