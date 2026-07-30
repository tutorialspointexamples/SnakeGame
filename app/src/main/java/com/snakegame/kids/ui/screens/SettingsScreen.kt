package com.snakegame.kids.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.snakegame.kids.data.UserSettings
import com.snakegame.kids.game.Difficulty
import com.snakegame.kids.ui.theme.LeafGreen
import com.snakegame.kids.ui.theme.PlaygroundGradient
import com.snakegame.kids.ui.theme.SoftCream
import com.snakegame.kids.ui.theme.SunnyYellow
import com.snakegame.kids.ui.theme.TextInk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: UserSettings,
    onBack: () -> Unit,
    onDifficulty: (Difficulty) -> Unit,
    onSound: (Boolean) -> Unit,
    onWrapWalls: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PlaygroundGradient)
    ) {
        TopAppBar(
            title = { Text("Settings", color = TextInk) },
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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            SettingsCard(title = "Difficulty") {
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

            SettingsCard(title = "Sound") {
                SettingSwitchRow(
                    label = "Sound effects",
                    checked = settings.soundEnabled,
                    onCheckedChange = onSound
                )
            }

            SettingsCard(title = "Walls") {
                SettingSwitchRow(
                    label = "Wrap around edges",
                    checked = settings.wrapWalls,
                    onCheckedChange = onWrapWalls
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = if (settings.wrapWalls) {
                        "Softer for kids — snake pops out the other side."
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
private fun SettingsCard(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SoftCream.copy(alpha = 0.92f), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = LeafGreen
        )
        Spacer(Modifier.height(12.dp))
        content()
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
