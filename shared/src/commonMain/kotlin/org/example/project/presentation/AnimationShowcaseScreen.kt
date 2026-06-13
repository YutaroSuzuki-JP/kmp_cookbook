package org.example.project.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp

@Composable
fun rememberShimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslation"
    )
    val shimmerColors = listOf(
        Color(0xFF2C2C3C),
        Color(0xFF3E3E52),
        Color(0xFF2C2C3C)
    )
    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnim - 300f, y = translateAnim - 300f),
        end = Offset(x = translateAnim, y = translateAnim)
    )
}

@Composable
fun AnimationShowcaseScreen(
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

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
                    tint = Color(0xFFEF5350)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Animations",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    color = Color(0xFFEF5350)
                )
                Text(
                    text = "Compose Transitions & Physics Playground",
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
            // Section 1: Spec Playground
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x1F2C2C3C)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Spring & Spec Playground",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "アニメーションスペック（物理ベースバネ、線形、キーフレーム）の挙動の違いを比較します。",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    // Animation target state
                    var targetRight by remember { mutableStateOf(false) }

                    // Choose animation spec
                    var selectedSpec by remember { mutableStateOf(0) } // 0: Spring, 1: Tween, 2: Keyframes

                    val specName = when (selectedSpec) {
                        0 -> "Spring (Bouncy)"
                        1 -> "Tween (Linear 1s)"
                        else -> "Keyframes (Complex)"
                    }

                    // Float state animates depending on spec
                    val offsetX by animateFloatAsState(
                        targetValue = if (targetRight) 220f else 0f,
                        animationSpec = when (selectedSpec) {
                            0 -> spring(
                                dampingRatio = Spring.DampingRatioHighBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                            1 -> tween(
                                durationMillis = 1000,
                                easing = LinearEasing
                            )
                            else -> keyframes {
                                durationMillis = 1200
                                0f at 0 with FastOutLinearInEasing
                                300f at 400 with LinearOutSlowInEasing
                                100f at 800 with FastOutSlowInEasing
                                220f at 1200
                            }
                        }
                    )

                    // Track box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF14141E))
                            .border(1.dp, Color.DarkGray, RoundedCornerShape(8.dp))
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        // The animated element
                        Box(
                            modifier = Modifier
                                .offset(x = offsetX.dp)
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Color(0xFFEF5350), Color(0xFFFF8A80))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { targetRight = !targetRight },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350))
                        ) {
                            Text("実行", color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        // Toggle Spec
                        Button(
                            onClick = { selectedSpec = (selectedSpec + 1) % 3 },
                            modifier = Modifier.weight(1.2f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C3C)),
                            border = BorderStroke(1.dp, Color.DarkGray)
                        ) {
                            Text(specName, color = Color.LightGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Section 2: Draggable Snapping Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x1F2C2C3C)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "2. Gesture Drag & Spring Snap",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "カードをドラッグして動かせます。指を離すとバネの物理演算で元の位置に気持ちよくスナップバックします。",
                        fontSize = 12.sp,
                        color = Color.LightGray,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(30.dp))

                    // Gesture states
                    val dragX = remember { Animatable(0f) }
                    val dragY = remember { Animatable(0f) }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Background placeholder target
                        Box(
                            modifier = Modifier
                                .size(140.dp, 90.dp)
                                .border(1.5.dp, Color.DarkGray.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("ここにドラッグ", color = Color.DarkGray, fontSize = 11.sp)
                        }

                        // Interactive Draggable Card
                        Card(
                            modifier = Modifier
                                .offset { IntOffset(dragX.value.roundToInt(), dragY.value.roundToInt()) }
                                .size(140.dp, 90.dp)
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragEnd = {
                                            coroutineScope.launch {
                                                launch {
                                                    dragX.animateTo(
                                                        targetValue = 0f,
                                                        animationSpec = spring(
                                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                                            stiffness = Spring.StiffnessMediumLow
                                                        )
                                                    )
                                                }
                                                launch {
                                                    dragY.animateTo(
                                                        targetValue = 0f,
                                                        animationSpec = spring(
                                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                                            stiffness = Spring.StiffnessMediumLow
                                                        )
                                                    )
                                                }
                                            }
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            coroutineScope.launch {
                                                dragX.snapTo(dragX.value + dragAmount.x)
                                                dragY.snapTo(dragY.value + dragAmount.y)
                                            }
                                        }
                                    )
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFEF5350)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "つかんで動かす",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "X: ${dragX.value.roundToInt()} Y: ${dragY.value.roundToInt()}",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }

            // Section 3: Animated Visibility Expand
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x1F2C2C3C)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "3. AnimatedVisibility Details",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "コンテンツの表示非表示を、スライド＆フェードでスムーズに切り替えます。",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    var expanded by remember { mutableStateOf(false) }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = !expanded }
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF2C2C3C))
                            .border(1.dp, Color(0xFF3E3E52), RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "アコーディオン式詳細リスト",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                if (expanded) "閉じる ▲" else "開く ▼",
                                color = Color(0xFFEF5350),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        AnimatedVisibility(
                            visible = expanded,
                            enter = slideInVertically { -it / 2 } + expandVertically() + fadeIn(),
                            exit = slideOutVertically { -it / 2 } + shrinkVertically() + fadeOut()
                        ) {
                            Column(modifier = Modifier.padding(top = 16.dp)) {
                                HorizontalDivider(color = Color.DarkGray)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "ここに詳細情報が表示されます。AnimatedVisibilityは、Composeで条件分岐UI（if文による描画有無）をシームレスに滑らかにするための非常に強力なユーティリティです。",
                                    color = Color.LightGray,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "• レイアウト崩れなしの動的リサイズ\n• Enter/Exitトランジションの自由な組み合わせ\n• 子コンポーネントへの伝播トランジション",
                                    color = Color(0xFFFF8A80),
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            // Section 4: Shimmer Loading Skeleton Effect
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x1F2C2C3C)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "4. Infinite Shimmer Loading (Skeleton)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "データ取得中の待ち時間を短く感じさせるスケルトンシマー効果です。",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    val shimmerBrush = rememberShimmerBrush()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Image placeholder
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(shimmerBrush)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Title line
                            Box(
                                modifier = Modifier
                                    .width(160.dp)
                                    .height(16.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(shimmerBrush)
                            )
                            // Subtitle line
                            Box(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(12.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(shimmerBrush)
                            )
                        }
                    }
                }
            }
        }
    }
}
