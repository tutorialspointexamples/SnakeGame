package com.w3stech.easysnake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.w3stech.easysnake.data.UserSettings
import com.w3stech.easysnake.game.Difficulty
import com.w3stech.easysnake.game.SnakeSkin
import com.w3stech.easysnake.ui.components.GlassIconButton
import com.w3stech.easysnake.ui.components.GlassPanel
import com.w3stech.easysnake.ui.theme.LeafGreen
import com.w3stech.easysnake.ui.theme.PlaygroundGradient
import com.w3stech.easysnake.ui.theme.SunnyYellow
import com.w3stech.easysnake.ui.theme.TextInk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: UserSettings,
    onBack: () -> Unit,
    onDifficulty: (Difficulty) -> Unit,
    onSound: (Boolean) -> Unit,
    onHaptics: (Boolean) -> Unit,
    onWrapWalls: (Boolean) -> Unit,
    onSelectSkin: (SnakeSkin) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PlaygroundGradient)
    ) {
        TopAppBar(
            title = { Text("Settings", color = TextInk) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GlassPanel(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Difficulty",
                    style = MaterialTheme.typography.titleLarge,
                    color = LeafGreen
                )
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Difficulty.entries.forEach { difficulty ->
                        FilterChip(
                            selected = settings.difficulty == difficulty,
                            onClick = { onDifficulty(difficulty) },
                            label = { Text(difficulty.label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SunnyYellow,
                                selectedLabelColor = TextInk
                            )
                        )
                    }
                }
            }

            GlassPanel(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Snake skin",
                    style = MaterialTheme.typography.titleLarge,
                    color = LeafGreen
                )
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SnakeSkin.entries.forEach { skin ->
                        val unlocked = skin.isUnlocked(settings.unlockedStickers)
                        val selected = settings.selectedSkin == skin
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(skin.bodyA)
                                    .then(
                                        if (selected) {
                                            Modifier.border(3.dp, LeafGreen, CircleShape)
                                        } else {
                                            Modifier
                                        }
                                    )
                                    .clickable(enabled = unlocked) { onSelectSkin(skin) },
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(skin.head)
                                )
                                if (!unlocked) {
                                    Text("Locked", style = MaterialTheme.typography.labelMedium)
                                }
                            }
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = skin.label,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (unlocked) TextInk else TextInk.copy(alpha = 0.45f)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Unlock Sunny at score 10, Berry at score 25.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextInk.copy(alpha = 0.75f)
                )
            }

            GlassPanel(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Feedback",
                    style = MaterialTheme.typography.titleLarge,
                    color = LeafGreen
                )
                Spacer(Modifier.height(12.dp))
                SettingSwitchRow(
                    label = "Sound effects",
                    checked = settings.soundEnabled,
                    onCheckedChange = onSound
                )
                Spacer(Modifier.height(8.dp))
                SettingSwitchRow(
                    label = "Haptics",
                    checked = settings.hapticsEnabled,
                    onCheckedChange = onHaptics
                )
            }

            GlassPanel(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Walls",
                    style = MaterialTheme.typography.titleLarge,
                    color = LeafGreen
                )
                Spacer(Modifier.height(12.dp))
                SettingSwitchRow(
                    label = "Wrap around edges",
                    checked = settings.wrapWalls,
                    onCheckedChange = onWrapWalls
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = if (settings.wrapWalls) {
                        "Softer mode — snake pops out the other side."
                    } else {
                        "Classic mode — hitting a wall ends the game."
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextInk
                )
            }
        }
    }
}

@Composable
private fun SettingSwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = TextInk)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = LeafGreen,
                checkedThumbColor = Color.White
            )
        )
    }
}
