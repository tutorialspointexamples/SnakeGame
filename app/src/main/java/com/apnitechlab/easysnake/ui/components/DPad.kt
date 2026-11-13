package com.apnitechlab.easysnake.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.apnitechlab.easysnake.game.Direction
import com.apnitechlab.easysnake.ui.theme.LeafGreen
import com.apnitechlab.easysnake.ui.theme.SunnyYellow

@Composable
fun DPad(
    onDirection: (Direction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DPadKey(Icons.Filled.KeyboardArrowUp, "Up") { onDirection(Direction.UP) }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(64.dp)
            .shadow(4.dp, CircleShape)
            .background(SunnyYellow, CircleShape),
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = LeafGreen
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(36.dp)
        )
    }
}
