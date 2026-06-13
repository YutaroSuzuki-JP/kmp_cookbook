package org.example.project.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class GridItem(
    val id: Int,
    val title: String,
    val height: Int,
    val startColor: Color,
    val endColor: Color,
    val content: String
)

@Composable
fun LayoutShowcaseScreen(
    onBack: () -> Unit
) {
    // Background brush
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1E1E2C),
            Color(0xFF14141E)
        )
    )

    val gridItems = remember {
        listOf(
            GridItem(1, "コラージュ", 140, Color(0xFF42A5F5), Color(0xFF0077C2), "スタッガードグリッドは高さが異なるカードを自動配置します。"),
            GridItem(2, "インスピレーション", 200, Color(0xFF26A69A), Color(0xFF00756A), "石積み（マニアック）レイアウトでコンテンツを余すことなく敷き詰めます。"),
            GridItem(3, "タイポグラフィ", 120, Color(0xFFAB47BC), Color(0xFF790e8b), "高密度のタイル配置。"),
            GridItem(4, "ミニマルカード", 160, Color(0xFFE5A93C), Color(0xFFb07a00), "高さの変化は動的で、テキスト量に応じます。"),
            GridItem(5, "グラデーション", 220, Color(0xFFEF5350), Color(0xFFb61827), "異なるカラースペクトルを持つカードがランダムに並ぶ美しいグリッド。"),
            GridItem(6, "ポートフォリオ", 110, Color(0xFF26C6DA), Color(0xFF0095a8), "デザインアイデアのストックに。")
        )
    }

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
                    tint = Color(0xFF42A5F5)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Advanced Layouts",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    color = Color(0xFF42A5F5)
                )
                Text(
                    text = "Staggered Masonry Grids & BoxConstraints",
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: Responsive width simulation
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x1F2C2C3C)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Container Queries (Responsive Size Simulator)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "スライダーでコンテナ幅を変化させると、コンテナ内のBoxWithConstraintsが検知し、レイアウトが横並び（Row）と縦並び（Column）で自動的に切り替わります。",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    var containerWidthFraction by remember { mutableStateOf(0.7f) }

                    // Slider to change container width
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("幅調整:", color = Color.LightGray, fontSize = 11.sp, modifier = Modifier.width(50.dp))
                        Slider(
                            value = containerWidthFraction,
                            onValueChange = { containerWidthFraction = it },
                            modifier = Modifier.weight(1f),
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF42A5F5),
                                activeTrackColor = Color(0xFF42A5F5)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Simulated container boundary
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(containerWidthFraction.coerceAtLeast(0.4f))
                            .background(Color(0xFF14141E), RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFF2C2C3C), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                            val isWide = maxWidth >= 220.dp

                            if (isWide) {
                                // Wide configuration: Row
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                Brush.linearGradient(
                                                    colors = listOf(Color(0xFF42A5F5), Color(0xFF26C6DA))
                                                )
                                            )
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text("レスポンシブカード", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text("現在のレイアウト: Row (横並び)", color = Color(0xFF42A5F5), fontSize = 10.sp)
                                    }
                                }
                            } else {
                                // Narrow configuration: Column
                                Column(
                                    horizontalAlignment = Alignment.Start,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(40.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                Brush.linearGradient(
                                                    colors = listOf(Color(0xFF42A5F5), Color(0xFF26C6DA))
                                                )
                                            )
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("レスポンシブカード", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("レイアウト: Column (縦並び)", color = Color(0xFFEF5350), fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Section 2: Staggered grid
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x1F2C2C3C)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "2. Masonry Staggered Grid",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "LazyVerticalStaggeredGridによる変則グリッド。アイテムごとの異なる高さに合わせて自動で整列します。",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalItemSpacing = 8.dp
                    ) {
                        items(gridItems) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(item.height.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    item.startColor.copy(alpha = 0.2f),
                                                    item.endColor.copy(alpha = 0.05f)
                                                )
                                            )
                                        )
                                        .padding(12.dp)
                                ) {
                                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                                        Text(
                                            text = item.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = item.startColor
                                        )
                                        Text(
                                            text = item.content,
                                            fontSize = 10.sp,
                                            color = Color.LightGray,
                                            lineHeight = 14.sp,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(item.startColor.copy(alpha = 0.3f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "ID: ${item.id}",
                                                fontSize = 8.sp,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
