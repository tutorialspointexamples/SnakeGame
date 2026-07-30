package com.snakegame.kids.game

enum class Direction {
    UP, DOWN, LEFT, RIGHT;

    fun isOpposite(other: Direction): Boolean = when (this) {
        UP -> other == DOWN
        DOWN -> other == UP
        LEFT -> other == RIGHT
        RIGHT -> other == LEFT
    }
}

data class GridPoint(val x: Int, val y: Int)

enum class Difficulty(val tickMs: Long, val label: String) {
    EASY(280L, "Easy"),
    MEDIUM(200L, "Medium"),
    HARD(140L, "Hard");
}

enum class GameStatus {
    Ready,
    Running,
    Paused,
    GameOver
}

data class GameState(
    val cols: Int = 15,
    val rows: Int = 20,
    val snake: List<GridPoint> = emptyList(),
    val food: GridPoint = GridPoint(0, 0),
    val direction: Direction = Direction.RIGHT,
    val score: Int = 0,
    val status: GameStatus = GameStatus.Ready,
    val wrapWalls: Boolean = true,
    val difficulty: Difficulty = Difficulty.EASY,
    val justAte: Boolean = false,
    val justCollided: Boolean = false
)
