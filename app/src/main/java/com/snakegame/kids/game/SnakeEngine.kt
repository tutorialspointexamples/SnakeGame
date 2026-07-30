package com.snakegame.kids.game

import kotlin.random.Random

class SnakeEngine(
    private val cols: Int = 15,
    private val rows: Int = 20
) {
    private val directionQueue = ArrayDeque<Direction>()

    fun createInitialState(
        wrapWalls: Boolean,
        difficulty: Difficulty
    ): GameState {
        directionQueue.clear()
        val startX = cols / 2
        val startY = rows / 2
        val snake = listOf(
            GridPoint(startX, startY),
            GridPoint(startX - 1, startY),
            GridPoint(startX - 2, startY)
        )
        return GameState(
            cols = cols,
            rows = rows,
            snake = snake,
            food = spawnFood(snake),
            direction = Direction.RIGHT,
            score = 0,
            status = GameStatus.Ready,
            wrapWalls = wrapWalls,
            difficulty = difficulty,
            justAte = false,
            justCollided = false
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
            return state.copy(justAte = false, justCollided = false)
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
                justCollided = true
            )
        }

        val newHead = GridPoint(nextX, nextY)
        val ate = newHead == state.food
        val bodyWithoutTail = state.snake.dropLast(1)
        val collisionBody = if (ate) state.snake else bodyWithoutTail

        if (newHead in collisionBody) {
            return state.copy(
                status = GameStatus.GameOver,
                direction = direction,
                justAte = false,
                justCollided = true
            )
        }

        val grownSnake = listOf(newHead) + state.snake
        val movedSnake = listOf(newHead) + bodyWithoutTail

        return if (ate) {
            state.copy(
                snake = grownSnake,
                food = spawnFood(grownSnake),
                direction = direction,
                score = state.score + 1,
                justAte = true,
                justCollided = false
            )
        } else {
            state.copy(
                snake = movedSnake,
                direction = direction,
                justAte = false,
                justCollided = false
            )
        }
    }

    private fun spawnFood(snake: List<GridPoint>): GridPoint {
        val occupied = snake.toHashSet()
        val empty = buildList {
            for (y in 0 until rows) {
                for (x in 0 until cols) {
                    val p = GridPoint(x, y)
                    if (p !in occupied) add(p)
                }
            }
        }
        if (empty.isEmpty()) return snake.first()
        return empty[Random.nextInt(empty.size)]
    }
}
