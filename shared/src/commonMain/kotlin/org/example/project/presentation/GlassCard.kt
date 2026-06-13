package org.example.project.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.project.theme.BrandTheme

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    blurRadius: Dp = BrandTheme.colorScheme.defaultBlurRadius,
    shape: Shape = RoundedCornerShape(24.dp),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .blur(blurRadius)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BrandTheme.colorScheme.glassCardBackground.copy(alpha = 0.15f),
                        BrandTheme.colorScheme.glassCardBackground.copy(alpha = 0.05f)
                    )
                ),
                shape = shape
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BrandTheme.colorScheme.glassBorder.copy(alpha = 0.3f),
                        BrandTheme.colorScheme.glassBorder.copy(alpha = 0.05f)
                    )
                ),
                shape = shape
            ),
        content = content
    )
}
