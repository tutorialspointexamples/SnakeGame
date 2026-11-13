package com.apnitechlab.easysnake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.apnitechlab.easysnake.game.AllStickers
import com.apnitechlab.easysnake.ui.theme.LeafGreen
import com.apnitechlab.easysnake.ui.theme.PlaygroundGradient
import com.apnitechlab.easysnake.ui.theme.SoftCream
import com.apnitechlab.easysnake.ui.theme.TextInk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    unlockedStickers: Set<String>,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PlaygroundGradient)
    ) {
        TopAppBar(
            title = { Text("Stickers", color = TextInk) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextInk
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Collect stickers by playing!",
                style = MaterialTheme.typography.bodyLarge,
                color = TextInk
            )
            Spacer(Modifier.height(4.dp))
            AllStickers.forEach { sticker ->
                val unlocked = sticker.id in unlockedStickers
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            SoftCream.copy(alpha = if (unlocked) 0.95f else 0.55f),
                            RoundedCornerShape(18.dp)
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (unlocked) sticker.emoji else "🔒",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(Modifier.width(20.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = sticker.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = if (unlocked) LeafGreen else TextInk.copy(alpha = 0.55f)
                        )
                        Text(
                            text = sticker.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextInk.copy(alpha = if (unlocked) 0.85f else 0.45f)
                        )
                    }
                }
            }
        }
    }
}
