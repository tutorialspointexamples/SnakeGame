package com.w3stech.easysnake.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.w3stech.easysnake.ui.theme.GlassWhite
import com.w3stech.easysnake.ui.theme.PrimaryButtonGradient
import com.w3stech.easysnake.ui.theme.TextInk

private val GlassShape = RoundedCornerShape(20.dp)
private val PillShape = RoundedCornerShape(22.dp)

@Composable
fun HudChip(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(6.dp, GlassShape, clip = false)
            .clip(GlassShape)
            .background(GlassWhite)
            .border(1.dp, Color.White.copy(alpha = 0.65f), GlassShape)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        content()
    }
}

@Composable
fun GlassPanel(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(24.dp), clip = false)
            .clip(RoundedCornerShape(24.dp))
            .background(GlassWhite)
            .border(1.dp, Color.White.copy(alpha = 0.7f), RoundedCornerShape(24.dp))
            .padding(18.dp)
    ) {
        content()
    }
}

@Composable
fun GlassIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(4.dp, CircleShape, clip = false)
            .clip(CircleShape)
            .background(GlassWhite)
            .border(1.dp, Color.White.copy(alpha = 0.7f), CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale = if (pressed) 0.97f else 1f

    Box(
        modifier = modifier
            .scale(scale)
            .height(56.dp)
            .shadow(8.dp, PillShape, clip = false)
            .clip(PillShape)
            .background(PrimaryButtonGradient, PillShape)
            .clickable(
                enabled = enabled,
                interactionSource = interaction,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
    }
}

@Composable
fun SecondaryOutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(52.dp)
            .clip(PillShape)
            .background(Color.White.copy(alpha = 0.55f), PillShape)
            .border(1.5.dp, TextInk.copy(alpha = 0.18f), PillShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = TextInk
        )
    }
}

@Composable
fun LegendChip(
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.72f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = TextInk
        )
    }
}

@Composable
fun ScoreHud(
    score: Int,
    bonusText: String?,
    scale: Float,
    modifier: Modifier = Modifier
) {
    HudChip(modifier = modifier) {
        Column {
            Text(
                text = "SCORE",
                style = MaterialTheme.typography.labelMedium,
                color = TextInk.copy(alpha = 0.55f)
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = score.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextInk,
                    modifier = Modifier.scale(scale)
                )
                if (bonusText != null) {
                    Spacer(Modifier.size(8.dp))
                    Text(
                        text = bonusText,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFFE39B00)
                    )
                }
            }
        }
    }
}

@Composable
fun TipCard(
    title: String,
    modifier: Modifier = Modifier,
    chips: @Composable RowScope.() -> Unit
) {
    GlassPanel(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = TextInk,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            content = chips
        )
    }
}
