package com.snakegame.kids.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.snakegame.kids.audio.SoundPlayer
import com.snakegame.kids.data.SettingsRepository
import com.snakegame.kids.data.UserSettings
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsRepository = SettingsRepository(application)
    private val soundPlayer = SoundPlayer(application)
    private val engine = SnakeEngine()

    val settings: StateFlow<UserSettings> = settingsRepository.settings
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserSettings())

    private val _gameState = MutableStateFlow(
        engine.createInitialState(wrapWalls = true, difficulty = Difficulty.EASY)
    )
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private var loopJob: Job? = null

    init {
        viewModelScope.launch {
            settings.collect { userSettings ->
                soundPlayer.setEnabled(userSettings.soundEnabled)
                _gameState.update { current ->
                    if (current.status == GameStatus.Ready) {
                        current.copy(
                            wrapWalls = userSettings.wrapWalls,
                            difficulty = userSettings.difficulty
                        )
                    } else {
                        current
                    }
                }
            }
        }
    }

    fun prepareNewGame() {
        stopLoop()
        engine.clearQueue()
        val user = settings.value
        _gameState.value = engine.createInitialState(
            wrapWalls = user.wrapWalls,
            difficulty = user.difficulty
        )
    }

    fun startOrResume() {
        val current = _gameState.value
        if (current.status == GameStatus.GameOver) {
            prepareNewGame()
        }
        _gameState.update {
            it.copy(
                status = GameStatus.Running,
                justAte = false,
                justCollided = false
            )
        }
        soundPlayer.playStart()
        startLoop()
    }

    fun pause() {
        if (_gameState.value.status != GameStatus.Running) return
        stopLoop()
        _gameState.update { it.copy(status = GameStatus.Paused) }
    }

    fun onDirection(direction: Direction) {
        val state = _gameState.value
        if (state.status != GameStatus.Running && state.status != GameStatus.Ready) return
        if (state.status == GameStatus.Ready) {
            _gameState.update { it.copy(status = GameStatus.Running) }
            startLoop()
            soundPlayer.playStart()
        }
        engine.enqueueDirection(_gameState.value.direction, direction)
    }

    fun setDifficulty(difficulty: Difficulty) {
        viewModelScope.launch { settingsRepository.setDifficulty(difficulty) }
    }

    fun setSoundEnabled(enabled: Boolean) {
        soundPlayer.setEnabled(enabled)
        viewModelScope.launch { settingsRepository.setSoundEnabled(enabled) }
    }

    fun setWrapWalls(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setWrapWalls(enabled) }
    }

    private fun startLoop() {
        if (loopJob?.isActive == true) return
        loopJob = viewModelScope.launch {
            while (isActive) {
                val tickMs = _gameState.value.difficulty.tickMs
                delay(tickMs)
                val before = _gameState.value
                if (before.status != GameStatus.Running) break
                val after = engine.tick(before)
                _gameState.value = after
                if (after.justAte) soundPlayer.playEat()
                if (after.justCollided || after.status == GameStatus.GameOver) {
                    if (after.justCollided) soundPlayer.playCollision()
                    settingsRepository.updateHighScoreIfNeeded(after.score)
                    stopLoop()
                    break
                }
            }
        }
    }

    private fun stopLoop() {
        loopJob?.cancel()
        loopJob = null
    }

    override fun onCleared() {
        stopLoop()
        soundPlayer.release()
        super.onCleared()
    }
}
