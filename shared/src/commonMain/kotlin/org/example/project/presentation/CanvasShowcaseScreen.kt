package org.example.project.presentation

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CanvasShowcaseScreen(
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1E1E2C),
            Color(0xFF14141E)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .safeContentPadding()
    ) {
        // App Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF2C2C3C))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "カタログに戻る",
                    tint = Color(0xFF26A69A)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Canvas & Drawing",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    color = Color(0xFF26A69A)
                )
                Text(
                    text = "Custom Draw Scope & Graphic Renderings",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.LightGray
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Section 1: Donut Chart
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x1F2C2C3C)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "1. Interactive Donut Chart",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "円弧の描画 (drawArc) によるグラフ表示。アニメーションで分割量が動的に展開します。",
                        fontSize = 12.sp,
                        color = Color.LightGray,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    var selectedIndex by remember { mutableStateOf(-1) }

                    // Animation of entry
                    val transition = rememberInfiniteTransition()
                    val pulseScale by transition.animateFloat(
                        initialValue = 1f,
                        targetValue = 1.05f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1200, easing = EaseInOutSine),
                            repeatMode = RepeatMode.Reverse
                        )
                    )

                    // Draw the donut
                    Box(
                        modifier = Modifier.size(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeWidth = 32.dp.toPx()
                            val width = size.width
                            val height = size.height
                            val radius = (width - strokeWidth * 2) / 2
                            val center = Offset(width / 2, height / 2)

                            // Pie pieces data: values 40%, 35%, 25%
                            val angles = listOf(144f, 126f, 90f) // Sums to 360f
                            val colors = listOf(Color(0xFF26A69A), Color(0xFFE5A93C), Color(0xFFEF5350))
                            var startAngle = -90f

                            for (i in angles.indices) {
                                val drawStroke = if (selectedIndex == i) {
                                    Stroke(width = strokeWidth * pulseScale)
                                } else {
                                    Stroke(width = strokeWidth)
                                }
                                val pad = if (selectedIndex == i) 8.dp.toPx() else 0f

                                drawArc(
                                    color = colors[i],
                                    startAngle = startAngle,
                                    sweepAngle = angles[i] - 2f, // leave minor gap
                                    useCenter = false,
                                    topLeft = Offset(strokeWidth / 2 + pad, strokeWidth / 2 + pad),
                                    size = Size(width - strokeWidth - pad * 2, height - strokeWidth - pad * 2),
                                    style = drawStroke
                                )
                                startAngle += angles[i]
                            }
                        }

                        // Center details label
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = when (selectedIndex) {
                                    0 -> "Teal"
                                    1 -> "Gold"
                                    2 -> "Coral"
                                    else -> "合計"
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = when (selectedIndex) {
                                    0 -> "40 %"
                                    1 -> "35 %"
                                    2 -> "25 %"
                                    else -> "100 %"
                                },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = when (selectedIndex) {
                                    0 -> Color(0xFF26A69A)
                                    1 -> Color(0xFFE5A93C)
                                    2 -> Color(0xFFEF5350)
                                    else -> Color.LightGray
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Indicator selectors
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        listOf("Teal (40%)", "Gold (35%)", "Coral (25%)").forEachIndexed { index, label ->
                            val color = listOf(Color(0xFF26A69A), Color(0xFFE5A93C), Color(0xFFEF5350))[index]
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        selectedIndex = if (selectedIndex == index) -1 else index
                                    }
                                    .background(if (selectedIndex == index) Color(0xFF2C2C3C) else Color.Transparent)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(label, color = Color.LightGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Section 2: Wavy Graph
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x1F2C2C3C)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "2. Wavy Line Graph (Bezier Path)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Pathのベジエ曲線 (cubicTo) を使った滑らかなトレンドグラフと、その下部のグラデーション塗りつぶし。",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Pulse animation for target dot
                    val infiniteTransition = rememberInfiniteTransition()
                    val pulseSize by infiniteTransition.animateFloat(
                        initialValue = 6f,
                        targetValue = 14f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000, easing = EaseInOutSine),
                            repeatMode = RepeatMode.Reverse
                        )
                    )
                    val pulseAlpha by infiniteTransition.animateFloat(
                        initialValue = 0.8f,
                        targetValue = 0.1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000, easing = EaseInOutSine),
                            repeatMode = RepeatMode.Reverse
                        )
                    )

                    // Pre-allocated Path objects to prevent GC allocations in DrawScope
                    val path = remember { Path() }
                    val fillPath = remember { Path() }

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .background(Color(0xFF14141E), RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFF2C2C3C), RoundedCornerShape(8.dp))
                    ) {
                        val width = size.width
                        val height = size.height
                        val points = listOf(
                            Offset(0f, height * 0.8f),
                            Offset(width * 0.2f, height * 0.7f),
                            Offset(width * 0.4f, height * 0.45f),
                            Offset(width * 0.6f, height * 0.5f),
                            Offset(width * 0.8f, height * 0.2f),
                            Offset(width, height * 0.35f)
                        )

                        // Path construction (Performance optimized)
                        path.reset()
                        path.moveTo(points[0].x, points[0].y)
                        for (i in 0 until points.size - 1) {
                            val from = points[i]
                            val to = points[i + 1]
                            val ctrl1 = Offset(from.x + (to.x - from.x) / 2f, from.y)
                            val ctrl2 = Offset(from.x + (to.x - from.x) / 2f, to.y)
                            path.cubicTo(ctrl1.x, ctrl1.y, ctrl2.x, ctrl2.y, to.x, to.y)
                        }

                        // Gradient fill path (Performance optimized)
                        fillPath.reset()
                        fillPath.addPath(path)
                        fillPath.lineTo(width, height)
                        fillPath.lineTo(0f, height)
                        fillPath.close()

                        // Draw gradient fill
                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF26A69A).copy(alpha = 0.35f),
                                    Color.Transparent
                                )
                            )
                        )

                        // Draw line
                        drawPath(
                            path = path,
                            color = Color(0xFF26A69A),
                            style = Stroke(width = 3.dp.toPx())
                        )

                        // Draw pulsing peak dot (at index 4: width * 0.8f, height * 0.2f)
                        val peak = points[4]
                        drawCircle(
                            color = Color(0xFF26A69A).copy(alpha = pulseAlpha),
                            radius = pulseSize.dp.toPx(),
                            center = peak
                        )
                        drawCircle(
                            color = Color(0xFF26A69A),
                            radius = 5.dp.toPx(),
                            center = peak
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 2.dp.toPx(),
                            center = peak
                        )
                    }
                }
            }

            // Section 3: Pulsing Radar
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x1F2C2C3C)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "3. Live Radar Wave (Infinite Loop)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "無限トランジションを用いた無限波紋（レーダーパルス）エフェクト。",
                        fontSize = 12.sp,
                        color = Color.LightGray,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    val radarTransition = rememberInfiniteTransition()

                    // Pulse waves
                    val wave1 by radarTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        )
                    )
                    val wave2 by radarTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart,
                            initialStartOffset = StartOffset(1000)
                        )
                    )

                    Canvas(
                        modifier = Modifier
                            .size(100.dp)
                    ) {
                        val center = Offset(size.width / 2, size.height / 2)
                        val maxRadius = size.width / 2

                        // Wave 1
                        drawCircle(
                            color = Color(0xFF26A69A).copy(alpha = 1f - wave1),
                            radius = maxRadius * wave1,
                            center = center,
                            style = Stroke(width = 2.dp.toPx())
                        )

                        // Wave 2
                        drawCircle(
                            color = Color(0xFF26A69A).copy(alpha = 1f - wave2),
                            radius = maxRadius * wave2,
                            center = center,
                            style = Stroke(width = 2.dp.toPx())
                        )

                        // Center core
                        drawCircle(
                            color = Color(0xFF26A69A),
                            radius = 6.dp.toPx(),
                            center = center
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}
