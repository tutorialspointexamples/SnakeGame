package com.apnitechlab.easysnake.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import com.apnitechlab.easysnake.game.ActivePowerUp
import com.apnitechlab.easysnake.game.Direction
import com.apnitechlab.easysnake.game.FoodType
import com.apnitechlab.easysnake.game.GameFeedback
import com.apnitechlab.easysnake.game.GameState
import com.apnitechlab.easysnake.game.GameStatus
import com.apnitechlab.easysnake.game.SnakeSkin
import com.apnitechlab.easysnake.ui.components.DPad
import com.apnitechlab.easysnake.ui.components.SnakeBoard
import com.apnitechlab.easysnake.ui.theme.FruitCoral
import com.apnitechlab.easysnake.ui.theme.LeafGreen
import com.apnitechlab.easysnake.ui.theme.PlaygroundGradient
import com.apnitechlab.easysnake.ui.theme.SoftCream
import com.apnitechlab.easysnake.ui.theme.SunnyYellow
import com.apnitechlab.easysnake.ui.theme.TextInk
import kotlinx.coroutines.flow.Flow

@Composable
fun GameScreen(
    state: GameState,
    skin: SnakeSkin,
    hapticsEnabled: Boolean,
    feedback: Flow<GameFeedback>,
    onDirection: (Direction) -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onHome: () -> Unit,
    onStart: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    LaunchedEffect(hapticsEnabled) {
        if (!hapticsEnabled) return@LaunchedEffect
        feedback.collect { event ->
            when (event) {
                GameFeedback.Eat -> haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                GameFeedback.Crash -> haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                GameFeedback.Win -> haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }
    }

    val scoreScale by animateFloatAsState(
        targetValue = if (state.justAte) 1.22f else 1f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = 500f),
        label = "scorePulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PlaygroundGradient)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Score ${state.score}",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextInk,
                        modifier = Modifier.graphicsLayer {
                            scaleX = scoreScale
                            scaleY = scoreScale
                        }
                    )
                    if (state.justAte && state.lastPointsGained > 1) {
                        Text(
                            text = "+${state.lastPointsGained}",
                            style = MaterialTheme.typography.labelLarge,
                            color = SunnyYellow
                        )
                    }
                }
                IconButton(onClick = onPause) {
                    Icon(
                        imageVector = Icons.Filled.Pause,
                        contentDescription = "Pause",
                        tint = TextInk
                    )
                }
            }

            PowerUpBanner(state)

            Spacer(Modifier.height(8.dp))

            SnakeBoard(
                state = state,
                skin = skin,
                onSwipe = onDirection,
                modifier = Modifier.weight(1f, fill = false)
            )

            if (state.status == GameStatus.Ready) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Swipe the board or tap the arrows!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextInk,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = fruitLegend(),
                    style = MaterialTheme.typography.labelLarge,
                    color = TextInk.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onStart,
                    colors = ButtonDefaults.buttonColors(containerColor = LeafGreen),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Start")
                }
            }

            Spacer(Modifier.height(12.dp))
            DPad(onDirection = onDirection)
            Spacer(Modifier.height(8.dp))
        }

        if (state.status == GameStatus.Paused) {
            PauseOverlay(onResume = onResume, onHome = onHome)
        }
    }
}

@Composable
private fun PowerUpBanner(state: GameState) {
    val label = when {
        state.activePowerUp == ActivePowerUp.SLOW && state.powerUpTicksRemaining > 0 ->
            "Slow-mo!"
        state.activePowerUp == ActivePowerUp.GHOST && state.powerUpTicksRemaining > 0 ->
            "Star power — ghost mode!"
        state.foodType == FoodType.GOLD -> "Gold fruit nearby!"
        state.foodType == FoodType.SLOW -> "Blue slow fruit nearby!"
        state.foodType == FoodType.STAR -> "Purple star fruit nearby!"
        else -> null
    } ?: return

    val color = when {
        state.activePowerUp == ActivePowerUp.SLOW -> Color(0xFF5B9BD5)
        state.activePowerUp == ActivePowerUp.GHOST -> Color(0xFFB57EDC)
        state.foodType == FoodType.GOLD -> SunnyYellow
        state.foodType == FoodType.SLOW -> Color(0xFF5B9BD5)
        state.foodType == FoodType.STAR -> Color(0xFFB57EDC)
        else -> FruitCoral
    }

    Text(
        text = label,
        style = MaterialTheme.typography.labelLarge,
        color = color,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        textAlign = TextAlign.Center
    )
}

private fun fruitLegend(): String =
    "🍎 fruit  ·  ⭐ gold  ·  💙 slow  ·  💜 ghost"

@Composable
private fun PauseOverlay(
    onResume: () -> Unit,
    onHome: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99000000)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .background(SoftCream, RoundedCornerShape(24.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Paused",
                style = MaterialTheme.typography.headlineMedium,
                color = LeafGreen
            )
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = onResume,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LeafGreen),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Resume")
            }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = onHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Home", color = TextInk)
            }
        }
    }
}
