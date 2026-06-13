package org.example.project.ads

import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun AdBanner(adUnitId: String, modifier: Modifier) {
    AndroidView(
        factory = { context ->
            LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setBackgroundColor(Color.parseColor("#E5A93C")) // Gold background
                val padding = (12 * context.resources.displayMetrics.density).toInt()
                setPadding(padding, padding, padding, padding)

                val label = TextView(context).apply {
                    text = "Android Native AdBanner Simulator\n[AdUnit: $adUnitId]"
                    setTextColor(Color.BLACK)
                    gravity = Gravity.CENTER
                    textSize = 12f
                }
                addView(label)
            }
        },
        modifier = modifier
    )
}
