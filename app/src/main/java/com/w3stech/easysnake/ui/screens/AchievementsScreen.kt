package com.w3stech.easysnake.ui.screens

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.w3stech.easysnake.game.AllStickers
import com.w3stech.easysnake.ui.components.GlassIconButton
import com.w3stech.easysnake.ui.components.GlassPanel
import com.w3stech.easysnake.ui.theme.LeafGreen
import com.w3stech.easysnake.ui.theme.PlaygroundGradient
import com.w3stech.easysnake.ui.theme.TextInk

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
                GlassIconButton(
                    onClick = onBack,
                    modifier = Modifier.padding(start = 8.dp),
                    size = 42.dp
                ) {
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
                text = "Collect stickers by playing",
                style = MaterialTheme.typography.bodyLarge,
                color = TextInk
            )
            Spacer(Modifier.height(4.dp))
            AllStickers.forEach { sticker ->
                val unlocked = sticker.id in unlockedStickers
                GlassPanel(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (unlocked) sticker.emoji else "•",
                            style = MaterialTheme.typography.headlineMedium,
                            color = if (unlocked) TextInk else TextInk.copy(alpha = 0.35f)
                        )
                        Spacer(Modifier.width(16.dp))
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
}
