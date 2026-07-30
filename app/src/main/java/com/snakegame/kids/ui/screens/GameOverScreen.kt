package com.snakegame.kids.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.snakegame.kids.ui.theme.FruitCoral
import com.snakegame.kids.ui.theme.LeafGreen
import com.snakegame.kids.ui.theme.PlaygroundGradient
import com.snakegame.kids.ui.theme.TextInk

@Composable
fun GameOverScreen(
    score: Int,
    highScore: Int,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit
) {
    val message = when {
        score == 0 -> "Nice try!"
        score >= highScore && score > 0 -> "Awesome score!"
        score >= 10 -> "You're getting great!"
        else -> "Nice try!"
    }
    val isNewBest = score > 0 && score >= highScore

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PlaygroundGradient)
            .padding(28.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.displayMedium,
                color = LeafGreen,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Score: $score",
                style = MaterialTheme.typography.headlineMedium,
                color = TextInk
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = if (isNewBest) "New best: $highScore" else "Best: $highScore",
                style = MaterialTheme.typography.titleLarge,
                color = FruitCoral
            )
            Spacer(Modifier.height(36.dp))
            Button(
                onClick = onPlayAgain,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LeafGreen),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Play Again", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = onHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Home", color = TextInk)
            }
        }
    }
}
