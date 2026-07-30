package com.snakegame.kids.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.snakegame.kids.game.Direction
import com.snakegame.kids.game.GameState
import com.snakegame.kids.ui.theme.BoardGrass
import com.snakegame.kids.ui.theme.BoardLine
import com.snakegame.kids.ui.theme.DeepLeaf
import com.snakegame.kids.ui.theme.FruitCoral
import com.snakegame.kids.ui.theme.LeafGreen
import com.snakegame.kids.ui.theme.MintGreen
import kotlin.math.abs

@Composable
fun SnakeBoard(
    state: GameState,
    onSwipe: (Direction) -> Unit,
    modifier: Modifier = Modifier
) {
    var totalDx by remember { mutableFloatStateOf(0f) }
    var totalDy by remember { mutableFloatStateOf(0f) }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(state.cols.toFloat() / state.rows.toFloat())
            .clip(RoundedCornerShape(20.dp))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        totalDx = 0f
                        totalDy = 0f
                    },
                    onDragEnd = {
                        val threshold = 40f
                        if (abs(totalDx) < threshold && abs(totalDy) < threshold) return@detectDragGestures
                        val direction = if (abs(totalDx) > abs(totalDy)) {
                            if (totalDx > 0) Direction.RIGHT else Direction.LEFT
                        } else {
                            if (totalDy > 0) Direction.DOWN else Direction.UP
                        }
                        onSwipe(direction)
                        totalDx = 0f
                        totalDy = 0f
                    },
                    onDragCancel = {
                        totalDx = 0f
                        totalDy = 0f
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        totalDx += dragAmount.x
                        totalDy += dragAmount.y
                    }
                )
            }
    ) {
        val cellW = size.width / state.cols
        val cellH = size.height / state.rows

        drawRoundRect(
            color = BoardGrass,
            cornerRadius = CornerRadius(20.dp.toPx(), 20.dp.toPx())
        )

        for (x in 0..state.cols) {
            val px = x * cellW
            drawLine(
                color = BoardLine,
                start = Offset(px, 0f),
                end = Offset(px, size.height),
                strokeWidth = 1.dp.toPx()
            )
        }
        for (y in 0..state.rows) {
            val py = y * cellH
            drawLine(
                color = BoardLine,
                start = Offset(0f, py),
                end = Offset(size.width, py),
                strokeWidth = 1.dp.toPx()
            )
        }

        val food = state.food
        val foodPad = cellW * 0.18f
        drawRoundRect(
            color = FruitCoral,
            topLeft = Offset(food.x * cellW + foodPad, food.y * cellH + foodPad),
            size = Size(cellW - foodPad * 2, cellH - foodPad * 2),
            cornerRadius = CornerRadius(cellW * 0.35f, cellH * 0.35f)
        )
        drawCircle(
            color = LeafGreen,
            radius = cellW * 0.08f,
            center = Offset(food.x * cellW + cellW * 0.55f, food.y * cellH + foodPad)
        )

        state.snake.forEachIndexed { index, point ->
            val pad = if (index == 0) cellW * 0.08f else cellW * 0.14f
            val color = if (index == 0) DeepLeaf else if (index % 2 == 0) LeafGreen else MintGreen
            drawRoundRect(
                color = color,
                topLeft = Offset(point.x * cellW + pad, point.y * cellH + pad),
                size = Size(cellW - pad * 2, cellH - pad * 2),
                cornerRadius = CornerRadius(cellW * 0.35f, cellH * 0.35f)
            )
            if (index == 0) {
                val eyeOffsetX = when (state.direction) {
                    Direction.LEFT -> -cellW * 0.12f
                    Direction.RIGHT -> cellW * 0.12f
                    else -> 0f
                }
                val eyeOffsetY = when (state.direction) {
                    Direction.UP -> -cellH * 0.12f
                    Direction.DOWN -> cellH * 0.12f
                    else -> 0f
                }
                val cx = point.x * cellW + cellW / 2 + eyeOffsetX
                val cy = point.y * cellH + cellH / 2 + eyeOffsetY
                drawCircle(Color.White, cellW * 0.12f, Offset(cx - cellW * 0.14f, cy))
                drawCircle(Color.White, cellW * 0.12f, Offset(cx + cellW * 0.14f, cy))
                drawCircle(Color(0xFF1B3A2A), cellW * 0.05f, Offset(cx - cellW * 0.14f, cy))
                drawCircle(Color(0xFF1B3A2A), cellW * 0.05f, Offset(cx + cellW * 0.14f, cy))
            }
        }
    }
}
