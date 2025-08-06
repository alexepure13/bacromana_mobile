package com.nxt64software.bacromana

import android.graphics.Typeface
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.widget.ScrollView
import android.widget.TextView
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.toArgb
import androidx.core.text.HtmlCompat
import androidx.core.text.util.LinkifyCompat

@Composable
fun StaticPage(title: String, body: String) {
    val isDark = isSystemInDarkTheme()
    val backgroundColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface
    val linkColor = if (isDark) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    val highlightColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            ScrollView(ctx).apply {
                setBackgroundColor(backgroundColor.toArgb())
                val tv = TextView(ctx).apply {
                    movementMethod = LinkMovementMethod.getInstance()
                    setLineSpacing(0f, 1.2f)
                    typeface = Typeface.SANS_SERIF
                    setTextColor(textColor.toArgb())
                    setLinkTextColor(linkColor.toArgb())
                    setHighlightColor(highlightColor.toArgb())
                    // auto-link fallback
                    LinkifyCompat.addLinks(this, Linkify.ALL)
                }
                addView(tv)
            }
        },
        update = { scroll ->
            val tv = (scroll.getChildAt(0) as? TextView) ?: return@AndroidView
            val combined = """
                <div style="color:${toHex(textColor)};">
                  <h2>$title</h2>
                  <div>$body</div>
                </div>
            """.trimIndent()
            tv.setBackgroundColor(backgroundColor.toArgb())
            tv.setTextColor(textColor.toArgb())
            tv.setLinkTextColor(linkColor.toArgb())
            tv.setHighlightColor(highlightColor.toArgb())
            tv.text = HtmlCompat.fromHtml(combined, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    )
}

private fun toHex(color: androidx.compose.ui.graphics.Color): String {
    val r = (color.red * 255).toInt().coerceIn(0, 255)
    val g = (color.green * 255).toInt().coerceIn(0, 255)
    val b = (color.blue * 255).toInt().coerceIn(0, 255)
    return String.format("#%02X%02X%02X", r, g, b)
}
