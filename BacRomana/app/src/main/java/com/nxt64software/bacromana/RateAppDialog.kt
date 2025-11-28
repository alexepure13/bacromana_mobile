package com.nxt64software.bacromana

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateAppDialog(
    onDismiss: () -> Unit,
    onRateNow: (rating: Int) -> Unit,
    onRateLater: () -> Unit,
    onNeverShowAgain: () -> Unit
) {
    val context = LocalContext.current
    var selectedRating by remember { mutableStateOf(5) } // Default 5 stars

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // App icon or emoji
                Text(
                    text = "⭐",
                    style = MaterialTheme.typography.displayMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.rate_app_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.rate_app_message),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Interactive star rating
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    repeat(5) { index ->
                        val starIndex = index + 1
                        Icon(
                            imageVector = if (starIndex <= selectedRating) Icons.Filled.Star else Icons.Filled.StarBorder,
                            contentDescription = "Stea $starIndex",
                            tint = if (starIndex <= selectedRating) Color(0xFFFFD700) else MaterialTheme.colorScheme.outline,
                            modifier = Modifier
                                .size(48.dp)
                                .clickable { selectedRating = starIndex }
                                .padding(4.dp)
                        )
                    }
                }

                Text(
                    text = stringResource(id = R.string.rate_app_select_stars),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onRateNow(selectedRating) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.rate_app_rate_now),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onRateLater,
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.rate_app_later),
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1
                            )
                        }

                        OutlinedButton(
                            onClick = onNeverShowAgain,
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.rate_app_never),
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

// Function to open Google Play Store for rating
fun openPlayStoreForRating(context: Context) {
    val packageName = context.packageName
    try {
        // Try to open directly in Play Store app
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
    } catch (e: Exception) {
        // Fallback to web browser
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
    }
}