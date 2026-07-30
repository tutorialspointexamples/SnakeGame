package com.snakegame.kids.ui.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.snakegame.kids.game.Direction
import com.snakegame.kids.game.GameState
import com.snakegame.kids.game.GameStatus
import com.snakegame.kids.ui.components.DPad
import com.snakegame.kids.ui.components.SnakeBoard
import com.snakegame.kids.ui.theme.LeafGreen
import com.snakegame.kids.ui.theme.PlaygroundGradient
import com.snakegame.kids.ui.theme.SoftCream
import com.snakegame.kids.ui.theme.TextInk

@Composable
fun GameScreen(
    state: GameState,
    onDirection: (Direction) -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onHome: () -> Unit,
    onStart: () -> Unit
) {
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
                Text(
                    text = "Score ${state.score}",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextInk
                )
                IconButton(onClick = onPause) {
                    Icon(
                        imageVector = Icons.Filled.Pause,
                        contentDescription = "Pause",
                        tint = TextInk
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            SnakeBoard(
                state = state,
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
