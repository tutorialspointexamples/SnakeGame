package com.w3stech.easysnake.game

object StickerId {
    const val FIRST_FRUIT = "first_fruit"
    const val SCORE_10 = "score_10"
    const val SCORE_25 = "score_25"
    const val SCORE_50 = "score_50"
    const val PLAY_5 = "play_5"
    const val FILL_BOARD = "fill_board"
}

data class Sticker(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String
)

val AllStickers = listOf(
    Sticker(StickerId.FIRST_FRUIT, "First Bite", "Eat your first fruit", "🍎"),
    Sticker(StickerId.SCORE_10, "Growing Fast", "Reach score 10", "⭐"),
    Sticker(StickerId.SCORE_25, "Garden Pro", "Reach score 25", "🌟"),
    Sticker(StickerId.SCORE_50, "Super Snake", "Reach score 50", "🏆"),
    Sticker(StickerId.PLAY_5, "Play Buddy", "Finish 5 games", "🎮"),
    Sticker(StickerId.FILL_BOARD, "Full Garden", "Fill the whole board", "🌈")
)
