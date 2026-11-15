package com.w3stech.easysnake.game

import androidx.compose.ui.graphics.Color

enum class SnakeSkin(
    val id: String,
    val label: String,
    val head: Color,
    val bodyA: Color,
    val bodyB: Color,
    val unlockStickerId: String?
) {
    CLASSIC(
        id = "classic",
        label = "Classic",
        head = Color(0xFF2E8B57),
        bodyA = Color(0xFF3FAF6C),
        bodyB = Color(0xFF6FCF97),
        unlockStickerId = null
    ),
    SUNNY(
        id = "sunny",
        label = "Sunny",
        head = Color(0xFFE0A100),
        bodyA = Color(0xFFF4C95F),
        bodyB = Color(0xFFFFE08A),
        unlockStickerId = StickerId.SCORE_10
    ),
    BERRY(
        id = "berry",
        label = "Berry",
        head = Color(0xFF6B3FA0),
        bodyA = Color(0xFF9B6BD6),
        bodyB = Color(0xFFC9A6F0),
        unlockStickerId = StickerId.SCORE_25
    );

    fun isUnlocked(unlockedStickers: Set<String>): Boolean =
        unlockStickerId == null || unlockStickerId in unlockedStickers

    companion object {
        fun fromId(id: String?): SnakeSkin =
            entries.find { it.id == id } ?: CLASSIC
    }
}
