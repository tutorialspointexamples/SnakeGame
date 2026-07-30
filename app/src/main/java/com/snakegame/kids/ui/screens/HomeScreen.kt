package com.snakegame.kids.ui.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.snakegame.kids.ui.theme.DeepLeaf
import com.snakegame.kids.ui.theme.FruitCoral
import com.snakegame.kids.ui.theme.LeafGreen
import com.snakegame.kids.ui.theme.MintGreen
import com.snakegame.kids.ui.theme.PlaygroundGradient
import com.snakegame.kids.ui.theme.SunnyYellow
import com.snakegame.kids.ui.theme.TextInk

@Composable
fun HomeScreen(
    highScore: Int,
    onPlay: () -> Unit,
    onSettings: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PlaygroundGradient)
    ) {
        IconButton(
            onClick = onSettings,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Settings",
                tint = TextInk
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Kids Snake",
                style = MaterialTheme.typography.displayMedium,
                color = LeafGreen,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Slide or tap to grow your friendly snake!",
                style = MaterialTheme.typography.bodyLarge,
                color = TextInk,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(28.dp))
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(SunnyYellow),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(88.dp)) {
                    val unit = size.minDimension / 6f
                    val segments = listOf(
                        Offset(unit * 0.6f, unit * 3.2f),
                        Offset(unit * 1.7f, unit * 3.2f),
                        Offset(unit * 2.8f, unit * 3.2f),
                        Offset(unit * 3.9f, unit * 2.2f)
                    )
                    segments.forEachIndexed { index, origin ->
                        drawRoundRect(
                            color = if (index == segments.lastIndex) DeepLeaf else if (index % 2 == 0) LeafGreen else MintGreen,
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
            Spacer(Modifier.height(28.dp))
            Text(
                text = "Best score: $highScore",
                style = MaterialTheme.typography.titleLarge,
                color = FruitCoral
            )
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = onPlay,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LeafGreen)
            ) {
                Text("Play", style = MaterialTheme.typography.titleLarge)
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = onSettings,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Settings", color = TextInk)
            }
        }
    }
}
