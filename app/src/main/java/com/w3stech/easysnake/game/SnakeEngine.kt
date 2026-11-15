package com.w3stech.easysnake.game

import kotlin.random.Random

class SnakeEngine(
    private val cols: Int = 15,
    private val rows: Int = 20
) {
    private val directionQueue = ArrayDeque<Direction>()
    private var nextSpecialAt = Random.nextInt(5, 9)

    fun createInitialState(
        wrapWalls: Boolean,
        difficulty: Difficulty
    ): GameState {
        directionQueue.clear()
        nextSpecialAt = Random.nextInt(5, 9)
        val startX = cols / 2
        val startY = rows / 2
        val snake = listOf(
            GridPoint(startX, startY),
            GridPoint(startX - 1, startY),
            GridPoint(startX - 2, startY)
        )
        val spawn = requireNotNull(spawnFood(snake, foodsEaten = 0)) {
            "Initial board must have empty cells"
        }
        val food = spawn.first
        val foodType = spawn.second
        return GameState(
            cols = cols,
            rows = rows,
            snake = snake,
            food = food,
            foodType = foodType,
            direction = Direction.RIGHT,
            score = 0,
            foodsEaten = 0,
            status = GameStatus.Ready,
            wrapWalls = wrapWalls,
            difficulty = difficulty,
            activePowerUp = ActivePowerUp.NONE,
            powerUpTicksRemaining = 0,
            justAte = false,
            justCollided = false,
            lastPointsGained = 0
        )
    }

    fun enqueueDirection(current: Direction, next: Direction) {
        val lastQueued = directionQueue.lastOrNull() ?: current
        if (next == lastQueued || next.isOpposite(lastQueued)) return
        if (directionQueue.size >= 2) return
        directionQueue.addLast(next)
    }

    fun clearQueue() {
        directionQueue.clear()
    }

    fun tick(state: GameState): GameState {
        if (state.status != GameStatus.Running) {
            return state.copy(justAte = false, justCollided = false, lastPointsGained = 0)
        }

        var direction = state.direction
        while (directionQueue.isNotEmpty()) {
            val candidate = directionQueue.removeFirst()
            if (!candidate.isOpposite(direction)) {
                direction = candidate
                break
            }
        }

        val head = state.snake.first()
        var nextX = head.x + when (direction) {
            Direction.LEFT -> -1
            Direction.RIGHT -> 1
            else -> 0
        }
        var nextY = head.y + when (direction) {
            Direction.UP -> -1
            Direction.DOWN -> 1
            else -> 0
        }

        if (state.wrapWalls) {
            nextX = (nextX + state.cols) % state.cols
            nextY = (nextY + state.rows) % state.rows
        } else if (nextX !in 0 until state.cols || nextY !in 0 until state.rows) {
            return state.copy(
                status = GameStatus.GameOver,
                direction = direction,
                justAte = false,
                justCollided = true,
                lastPointsGained = 0
            )
        }

        val newHead = GridPoint(nextX, nextY)
        val ate = newHead == state.food
        val bodyWithoutTail = state.snake.dropLast(1)
        val collisionBody = if (ate) state.snake else bodyWithoutTail
        val ghost = state.activePowerUp == ActivePowerUp.GHOST && state.powerUpTicksRemaining > 0

        if (!ghost && newHead in collisionBody) {
            return state.copy(
                status = GameStatus.GameOver,
                direction = direction,
                justAte = false,
                justCollided = true,
                lastPointsGained = 0
            )
        }

        val powerTicks = if (state.powerUpTicksRemaining > 0) {
            state.powerUpTicksRemaining - 1
        } else {
            0
        }
        val activePower = if (powerTicks > 0) state.activePowerUp else ActivePowerUp.NONE

        val grownSnake = listOf(newHead) + state.snake
        val movedSnake = listOf(newHead) + bodyWithoutTail

        return if (ate) {
            applyEat(
                state = state,
                snake = grownSnake,
                direction = direction,
                powerTicks = powerTicks,
                activePower = activePower
            )
        } else {
            state.copy(
                snake = movedSnake,
                direction = direction,
                activePowerUp = activePower,
                powerUpTicksRemaining = powerTicks,
                justAte = false,
                justCollided = false,
                lastPointsGained = 0
            )
        }
    }

    private fun applyEat(
        state: GameState,
        snake: List<GridPoint>,
        direction: Direction,
        powerTicks: Int,
        activePower: ActivePowerUp
    ): GameState {
        val points = when (state.foodType) {
            FoodType.GOLD -> 3
            else -> 1
        }
        val foodsEaten = state.foodsEaten + 1
        var nextPower = activePower
        var nextTicks = powerTicks

        when (state.foodType) {
            FoodType.SLOW -> {
                nextPower = ActivePowerUp.SLOW
                nextTicks = GameState.POWER_UP_TICKS
            }
            FoodType.STAR -> {
                nextPower = ActivePowerUp.GHOST
                nextTicks = GameState.POWER_UP_TICKS
            }
            else -> Unit
        }

        val spawn = spawnFood(snake, foodsEaten)
        if (spawn == null) {
            return state.copy(
                snake = snake,
                direction = direction,
                score = state.score + points,
                foodsEaten = foodsEaten,
                status = GameStatus.Won,
                activePowerUp = ActivePowerUp.NONE,
                powerUpTicksRemaining = 0,
                justAte = true,
                justCollided = false,
                lastPointsGained = points
            )
        }

        val (food, foodType) = spawn
        return state.copy(
            snake = snake,
            food = food,
            foodType = foodType,
            direction = direction,
            score = state.score + points,
            foodsEaten = foodsEaten,
            activePowerUp = nextPower,
            powerUpTicksRemaining = nextTicks,
            justAte = true,
            justCollided = false,
            lastPointsGained = points
        )
    }

    private fun spawnFood(
        snake: List<GridPoint>,
        foodsEaten: Int
    ): Pair<GridPoint, FoodType>? {
        val occupied = snake.toHashSet()
        val empty = buildList {
            for (y in 0 until rows) {
                for (x in 0 until cols) {
                    val p = GridPoint(x, y)
                    if (p !in occupied) add(p)
                }
            }
        }
        if (empty.isEmpty()) return null

        val point = empty[Random.nextInt(empty.size)]
        val type = if (foodsEaten > 0 && foodsEaten >= nextSpecialAt) {
            nextSpecialAt = foodsEaten + Random.nextInt(5, 9)
            listOf(FoodType.GOLD, FoodType.SLOW, FoodType.STAR).random()
        } else {
            FoodType.NORMAL
        }
        return point to type
    }
}
