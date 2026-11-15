package com.w3stech.easysnake.game

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

enum class FoodType {
    NORMAL,
    GOLD,
    SLOW,
    STAR
}

enum class ActivePowerUp {
    NONE,
    SLOW,
    GHOST
}

enum class GameStatus {
    Ready,
    Running,
    Paused,
    GameOver,
    Won
}

data class GameState(
    val cols: Int = 15,
    val rows: Int = 20,
    val snake: List<GridPoint> = emptyList(),
    val food: GridPoint = GridPoint(0, 0),
    val foodType: FoodType = FoodType.NORMAL,
    val direction: Direction = Direction.RIGHT,
    val score: Int = 0,
    val foodsEaten: Int = 0,
    val status: GameStatus = GameStatus.Ready,
    val wrapWalls: Boolean = true,
    val difficulty: Difficulty = Difficulty.EASY,
    val activePowerUp: ActivePowerUp = ActivePowerUp.NONE,
    val powerUpTicksRemaining: Int = 0,
    val justAte: Boolean = false,
    val justCollided: Boolean = false,
    val lastPointsGained: Int = 0
) {
    /** Base difficulty tick with progressive speed-up and optional Slow fruit. */
    fun effectiveTickMs(): Long {
        val steps = foodsEaten / SPEED_RAMP_EVERY
        val ramped = (difficulty.tickMs - steps * SPEED_RAMP_MS)
            .coerceAtLeast(MIN_TICK_MS)
        return if (activePowerUp == ActivePowerUp.SLOW) {
            ramped + SLOW_BONUS_MS
        } else {
            ramped
        }
    }

    companion object {
        const val SPEED_RAMP_EVERY = 5
        const val SPEED_RAMP_MS = 15L
        const val MIN_TICK_MS = 100L
        const val SLOW_BONUS_MS = 120L
        /** ~3s at Easy base (~280ms). */
        const val POWER_UP_TICKS = 11
    }
}
