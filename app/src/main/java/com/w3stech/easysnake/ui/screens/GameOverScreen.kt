package com.w3stech.easysnake.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.w3stech.easysnake.ads.BannerAd
import com.w3stech.easysnake.game.AllStickers
import com.w3stech.easysnake.ui.components.GlassPanel
import com.w3stech.easysnake.ui.components.GradientButton
import com.w3stech.easysnake.ui.components.SecondaryOutlineButton
import com.w3stech.easysnake.ui.theme.FruitCoral
import com.w3stech.easysnake.ui.theme.GhostViolet
import com.w3stech.easysnake.ui.theme.LeafGreen
import com.w3stech.easysnake.ui.theme.PlaygroundGradient
import com.w3stech.easysnake.ui.theme.SlowBlue
import com.w3stech.easysnake.ui.theme.SunnyYellow
import com.w3stech.easysnake.ui.theme.TextInk
import kotlin.random.Random

@Composable
fun GameOverScreen(
    score: Int,
    highScore: Int,
    won: Boolean,
    newlyUnlocked: Set<String>,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit
) {
    val message = when {
        won -> "You filled the garden!"
        score == 0 -> "Nice try!"
        score >= highScore && score > 0 -> "Awesome score!"
        score >= 10 -> "You're getting great!"
        else -> "Nice try!"
    }
    val isNewBest = !won && score > 0 && score >= highScore
    val unlockedLabels = AllStickers.filter { it.id in newlyUnlocked }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PlaygroundGradient)
    ) {
        if (won || isNewBest) {
            ConfettiOverlay()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GlassPanel(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.displayMedium,
                    color = LeafGreen,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "SCORE",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextInk.copy(alpha = 0.55f),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = score.toString(),
                    style = MaterialTheme.typography.displayMedium,
                    color = TextInk,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = when {
                        won -> "Best  $highScore"
                        isNewBest -> "New best  $highScore"
                        else -> "Best  $highScore"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    color = FruitCoral,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                if (unlockedLabels.isNotEmpty()) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "New stickers",
                        style = MaterialTheme.typography.titleLarge,
                        color = SunnyYellow,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    unlockedLabels.forEach { sticker ->
                        Text(
                            text = "${sticker.emoji}  ${sticker.title}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextInk,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(Modifier.height(28.dp))
            GradientButton(
                text = "Play Again",
                onClick = onPlayAgain,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            SecondaryOutlineButton(
                text = "Home",
                onClick = onHome,
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

@Composable
private fun ConfettiOverlay() {
    val pieces = remember {
        List(28) {
            ConfettiPiece(
                xFrac = Random.nextFloat(),
                speed = 0.35f + Random.nextFloat() * 0.55f,
                size = 8f + Random.nextFloat() * 10f,
                color = listOf(
                    LeafGreen,
                    FruitCoral,
                    SunnyYellow,
                    SlowBlue,
                    GhostViolet
                ).random()
            )
        }
    }
    val transition = rememberInfiniteTransition(label = "confetti")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "confettiT"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        pieces.forEachIndexed { index, piece ->
            val y = ((t * piece.speed + index * 0.03f) % 1.1f) * size.height
            val x = piece.xFrac * size.width
            drawCircle(
                color = piece.color,
                radius = piece.size,
                center = Offset(x, y)
            )
        }
    }
}

private data class ConfettiPiece(
    val xFrac: Float,
    val speed: Float,
    val size: Float,
    val color: Color
)
