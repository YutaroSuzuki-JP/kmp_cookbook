package org.example.project.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class CatalogItem(
    val id: Screen,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val tags: List<String>
)

@Composable
fun CatalogListScreen(
    onNavigate: (Screen) -> Unit
) {
    val items = remember {
        listOf(
            CatalogItem(
                id = Screen.MuseumExplorer,
                title = "Museum Explorer",
                description = "メトロポリタン美術館のオープンデータAPIを活用した、美術品の検索・詳細閲覧とお気に入り管理デモ。",
                icon = Icons.Default.Museum,
                iconBgColor = Color(0xFFE5A93C), // Gold
                tags = listOf("Ktor API", "Room Cache", "DI (Koin)")
            ),
            CatalogItem(
                id = Screen.AnimationShowcase,
                title = "Animations",
                description = "ComposeのアニメーションAPIを活用した、物理ベースのバネ、ドラッグ操作、カスタムイージングのデモ。",
                icon = Icons.Default.Animation,
                iconBgColor = Color(0xFFEF5350), // Coral Red
                tags = listOf("Spring Spec", "Drag Gesture", "AnimatedVisibility")
            ),
            CatalogItem(
                id = Screen.CanvasShowcase,
                title = "Canvas & Drawing",
                description = "Canvas APIを使用したインタラクティブな円グラフ（ドーナツチャート）や滑らかな波状折れ線グラフの実装。",
                icon = Icons.Default.Palette,
                iconBgColor = Color(0xFF26A69A), // Teal
                tags = listOf("Custom Draw", "Bezier Curve", "Infinite Animation")
            ),
            CatalogItem(
                id = Screen.ComponentShowcase,
                title = "Modern Components",
                description = "グラスモルフィズムカード、インタラクティブボタン、アニメーション付きボトムシートなどのショーケース。",
                icon = Icons.Default.Widgets,
                iconBgColor = Color(0xFFAB47BC), // Purple
                tags = listOf("Glassmorphism", "BottomSheet", "Ripple Effect")
            ),
            CatalogItem(
                id = Screen.LayoutShowcase,
                title = "Advanced Layouts",
                description = "高さの異なるカードを配置するスタッガードグリッドや、画面幅に応じた動的なレスポンシブレイアウト。",
                icon = Icons.Default.GridView,
                iconBgColor = Color(0xFF42A5F5), // Blue
                tags = listOf("StaggeredGrid", "Adaptive UI", "Dynamic Columns")
            )
        )
    }

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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "COMPOSE COOKBOOK",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Jetpack Compose デザイン & アニメーション カタログ",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.LightGray,
                textAlign = TextAlign.Center
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) { item ->
                CatalogCard(item = item, onClick = { onNavigate(item.id) })
            }
        }
    }
}

@Composable
fun CatalogCard(
    item: CatalogItem,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.96f else 1.0f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x1F2C2C3C) // Translucent for glassmorphic feel
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.15f),
                            Color.White.copy(alpha = 0.03f)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                // Item Icon
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(item.iconBgColor.copy(alpha = 0.2f))
                        .border(1.dp, item.iconBgColor.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = item.iconBgColor,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Metadata
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = item.description,
                        fontSize = 13.sp,
                        color = Color.LightGray.copy(alpha = 0.8f),
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Tags flow row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item.tags.forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF2C2C3C))
                                    .border(1.dp, Color(0xFF3E3E52), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = tag,
                                    fontSize = 10.sp,
                                    color = Color.LightGray,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Chevron Arrow
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Navigate",
                    tint = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(24.dp)
                )
            }
        }
    }
}
