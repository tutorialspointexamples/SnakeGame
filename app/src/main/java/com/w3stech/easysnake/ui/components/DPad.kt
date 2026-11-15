package com.w3stech.easysnake.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.w3stech.easysnake.game.Direction
import com.w3stech.easysnake.ui.theme.DeepTeal
import com.w3stech.easysnake.ui.theme.SunnyYellow

@Composable
fun DPad(
    onDirection: (Direction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        DPadKey(Icons.Filled.KeyboardArrowUp, "Up") { onDirection(Direction.UP) }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            DPadKey(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Left") {
                onDirection(Direction.LEFT)
            }
            DPadKey(Icons.Filled.KeyboardArrowDown, "Down") { onDirection(Direction.DOWN) }
            DPadKey(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Right") {
                onDirection(Direction.RIGHT)
            }
        }
    }
}

@Composable
private fun DPadKey(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.90f else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 500f),
        label = "dpadPress"
    )

    Box(
        modifier = Modifier
            .size(68.dp)
            .scale(scale)
            .shadow(8.dp, CircleShape, clip = false)
            .clip(CircleShape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        SunnyYellow,
                        SunnyYellow.copy(alpha = 0.82f)
                    )
                )
            )
            .border(1.5.dp, ColorWhiteSoft, CircleShape)
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = DeepTeal,
            modifier = Modifier.size(38.dp)
        )
    }
}

private val ColorWhiteSoft = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.55f)
