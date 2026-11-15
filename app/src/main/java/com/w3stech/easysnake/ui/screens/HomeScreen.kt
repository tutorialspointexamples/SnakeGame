package com.w3stech.easysnake.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.w3stech.easysnake.ads.BannerAd
import com.w3stech.easysnake.game.SnakeSkin
import com.w3stech.easysnake.ui.components.GlassIconButton
import com.w3stech.easysnake.ui.components.GradientButton
import com.w3stech.easysnake.ui.components.HudChip
import com.w3stech.easysnake.ui.components.SecondaryOutlineButton
import com.w3stech.easysnake.ui.theme.DeepTeal
import com.w3stech.easysnake.ui.theme.FruitCoral
import com.w3stech.easysnake.ui.theme.LeafGreen
import com.w3stech.easysnake.ui.theme.MintGreen
import com.w3stech.easysnake.ui.theme.PlaygroundGradient
import com.w3stech.easysnake.ui.theme.SunnyYellow
import com.w3stech.easysnake.ui.theme.TextInk

@Composable
fun HomeScreen(
    highScore: Int,
    stickerCount: Int,
    skin: SnakeSkin,
    onPlay: () -> Unit,
    onSettings: () -> Unit,
    onAchievements: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    val titleAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(650),
        label = "homeTitle"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PlaygroundGradient)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GlassIconButton(onClick = onAchievements) {
                Icon(
                    imageVector = Icons.Filled.EmojiEvents,
                    contentDescription = "Stickers",
                    tint = TextInk
                )
            }
            GlassIconButton(onClick = onSettings) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = TextInk
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)
                .padding(bottom = 64.dp)
                .alpha(titleAlpha),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "EasySnake",
                style = MaterialTheme.typography.displayMedium,
                color = DeepTeal,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Slide or tap to grow your friendly snake",
                style = MaterialTheme.typography.bodyLarge,
                color = TextInk.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(SunnyYellow, MintGreen.copy(alpha = 0.55f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(92.dp)) {
                    val unit = size.minDimension / 6f
                    val segments = listOf(
                        Offset(unit * 0.6f, unit * 3.2f),
                        Offset(unit * 1.7f, unit * 3.2f),
                        Offset(unit * 2.8f, unit * 3.2f),
                        Offset(unit * 3.9f, unit * 2.2f)
                    )
                    segments.forEachIndexed { index, origin ->
                        val color = when {
                            index == segments.lastIndex -> skin.head
                            index % 2 == 0 -> skin.bodyA
                            else -> skin.bodyB
                        }
                        drawRoundRect(
                            color = color,
                            topLeft = origin,
                            size = Size(unit * 1.15f, unit * 1.15f),
                            cornerRadius = CornerRadius(unit * 0.4f, unit * 0.4f)
                        )
                    }
                    val head = segments.last()
                    drawCircle(Color.White, unit * 0.18f, Offset(head.x + unit * 0.35f, head.y + unit * 0.35f))
                    drawCircle(Color.White, unit * 0.18f, Offset(head.x + unit * 0.75f, head.y + unit * 0.35f))
                    drawCircle(FruitCoral, unit * 0.45f, Offset(unit * 5.1f, unit * 1.3f))
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = "${skin.label} skin",
                style = MaterialTheme.typography.labelLarge,
                color = TextInk.copy(alpha = 0.65f)
            )
            Spacer(Modifier.height(18.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HudChip {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "BEST",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextInk.copy(alpha = 0.55f)
                        )
                        Text(
                            text = highScore.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            color = FruitCoral
                        )
                    }
                }
                HudChip {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "STICKERS",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextInk.copy(alpha = 0.55f)
                        )
                        Text(
                            text = "$stickerCount / 6",
                            style = MaterialTheme.typography.titleLarge,
                            color = LeafGreen
                        )
                    }
                }
            }
            Spacer(Modifier.height(28.dp))
            GradientButton(
                text = "Play",
                onClick = onPlay,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            SecondaryOutlineButton(
                text = "Stickers",
                onClick = onAchievements,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            SecondaryOutlineButton(
                text = "Settings",
                onClick = onSettings,
                modifier = Modifier.fillMaxWidth()
            )
        }

        BannerAd(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 4.dp)
        )
    }
}
