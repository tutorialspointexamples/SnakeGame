package com.w3stech.easysnake.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.w3stech.easysnake.ads.BannerAd
import com.w3stech.easysnake.game.ActivePowerUp
import com.w3stech.easysnake.game.Direction
import com.w3stech.easysnake.game.FoodType
import com.w3stech.easysnake.game.GameFeedback
import com.w3stech.easysnake.game.GameState
import com.w3stech.easysnake.game.GameStatus
import com.w3stech.easysnake.game.SnakeSkin
import com.w3stech.easysnake.ui.components.DPad
import com.w3stech.easysnake.ui.components.GlassIconButton
import com.w3stech.easysnake.ui.components.GlassPanel
import com.w3stech.easysnake.ui.components.GradientButton
import com.w3stech.easysnake.ui.components.HudChip
import com.w3stech.easysnake.ui.components.LegendChip
import com.w3stech.easysnake.ui.components.ScoreHud
import com.w3stech.easysnake.ui.components.SecondaryOutlineButton
import com.w3stech.easysnake.ui.components.SnakeBoard
import com.w3stech.easysnake.ui.components.TipCard
import com.w3stech.easysnake.ui.theme.FruitCoral
import com.w3stech.easysnake.ui.theme.GhostViolet
import com.w3stech.easysnake.ui.theme.LeafGreen
import com.w3stech.easysnake.ui.theme.PlaygroundGradient
import com.w3stech.easysnake.ui.theme.SlowBlue
import com.w3stech.easysnake.ui.theme.SunnyYellow
import com.w3stech.easysnake.ui.theme.TextInk
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
        targetValue = if (state.justAte) 1.18f else 1f,
        animationSpec = spring(dampingRatio = 0.42f, stiffness = 520f),
        label = "scorePulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PlaygroundGradient)
            .padding(horizontal = 14.dp, vertical = 12.dp)
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
                ScoreHud(
                    score = state.score,
                    bonusText = if (state.justAte && state.lastPointsGained > 1) {
                        "+${state.lastPointsGained}"
                    } else {
                        null
                    },
                    scale = scoreScale
                )
                GlassIconButton(onClick = onPause, size = 48.dp) {
                    Icon(
                        imageVector = Icons.Filled.Pause,
                        contentDescription = "Pause",
                        tint = TextInk
                    )
                }
            }

            PowerUpBanner(state)

            Spacer(Modifier.height(10.dp))

            SnakeBoard(
                state = state,
                skin = skin,
                onSwipe = onDirection,
                modifier = Modifier.weight(1f, fill = false)
            )

            if (state.status == GameStatus.Ready) {
                Spacer(Modifier.height(10.dp))
                TipCard(title = "Swipe the board or tap the arrows") {
                    LegendChip(label = "Fruit", color = FruitCoral)
                    LegendChip(label = "Gold", color = SunnyYellow)
                    LegendChip(label = "Slow", color = SlowBlue)
                    LegendChip(label = "Ghost", color = GhostViolet)
                }
                Spacer(Modifier.height(10.dp))
                GradientButton(
                    text = "Start",
                    onClick = onStart,
                    modifier = Modifier.fillMaxWidth(0.55f)
                )
            }

            Spacer(Modifier.height(8.dp))
            DPad(onDirection = onDirection)
            Spacer(Modifier.height(4.dp))
            BannerAd()
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
            "Slow-mo active"
        state.activePowerUp == ActivePowerUp.GHOST && state.powerUpTicksRemaining > 0 ->
            "Ghost mode"
        state.foodType == FoodType.GOLD -> "Gold fruit nearby"
        state.foodType == FoodType.SLOW -> "Slow fruit nearby"
        state.foodType == FoodType.STAR -> "Star fruit nearby"
        else -> null
    } ?: return

    val color = when {
        state.activePowerUp == ActivePowerUp.SLOW -> SlowBlue
        state.activePowerUp == ActivePowerUp.GHOST -> GhostViolet
        state.foodType == FoodType.GOLD -> SunnyYellow
        state.foodType == FoodType.SLOW -> SlowBlue
        state.foodType == FoodType.STAR -> GhostViolet
        else -> FruitCoral
    }

    Spacer(Modifier.height(8.dp))
    HudChip {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = color
        )
    }
}

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
        GlassPanel(modifier = Modifier.fillMaxWidth(0.88f)) {
            Text(
                text = "Paused",
                style = MaterialTheme.typography.headlineMedium,
                color = LeafGreen,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))
            GradientButton(
                text = "Resume",
                onClick = onResume,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))
            SecondaryOutlineButton(
                text = "Home",
                onClick = onHome,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
