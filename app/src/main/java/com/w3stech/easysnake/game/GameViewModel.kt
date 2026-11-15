package com.w3stech.easysnake.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.w3stech.easysnake.audio.SoundPlayer
import com.w3stech.easysnake.data.SettingsRepository
import com.w3stech.easysnake.data.UserSettings
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

sealed class GameFeedback {
    data object Eat : GameFeedback()
    data object Crash : GameFeedback()
    data object Win : GameFeedback()
}

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

    private val _feedback = MutableSharedFlow<GameFeedback>(extraBufferCapacity = 8)
    val feedback: SharedFlow<GameFeedback> = _feedback.asSharedFlow()

    private val _newlyUnlocked = MutableStateFlow<Set<String>>(emptySet())
    val newlyUnlocked: StateFlow<Set<String>> = _newlyUnlocked.asStateFlow()

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
        _newlyUnlocked.value = emptySet()
    }

    fun startOrResume() {
        val current = _gameState.value
        if (current.status == GameStatus.GameOver || current.status == GameStatus.Won) {
            prepareNewGame()
        }
        _gameState.update {
            it.copy(
                status = GameStatus.Running,
                justAte = false,
                justCollided = false,
                lastPointsGained = 0
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

    fun setHapticsEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setHapticsEnabled(enabled) }
    }

    fun setWrapWalls(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setWrapWalls(enabled) }
    }

    fun setSelectedSkin(skin: SnakeSkin) {
        viewModelScope.launch { settingsRepository.setSelectedSkin(skin) }
    }

    fun clearNewlyUnlocked() {
        _newlyUnlocked.value = emptySet()
    }

    private fun startLoop() {
        if (loopJob?.isActive == true) return
        loopJob = viewModelScope.launch {
            while (isActive) {
                val tickMs = _gameState.value.effectiveTickMs()
                delay(tickMs)
                val before = _gameState.value
                if (before.status != GameStatus.Running) break
                val after = engine.tick(before)
                _gameState.value = after

                if (after.justAte) {
                    soundPlayer.playEat()
                    _feedback.tryEmit(GameFeedback.Eat)
                }

                when (after.status) {
                    GameStatus.GameOver -> {
                        if (after.justCollided) {
                            soundPlayer.playCollision()
                            _feedback.tryEmit(GameFeedback.Crash)
                        }
                        _newlyUnlocked.value =
                            settingsRepository.recordGameFinished(after.score, won = false)
                        stopLoop()
                        break
                    }
                    GameStatus.Won -> {
                        soundPlayer.playEat()
                        _feedback.tryEmit(GameFeedback.Win)
                        _newlyUnlocked.value =
                            settingsRepository.recordGameFinished(after.score, won = true)
                        stopLoop()
                        break
                    }
                    else -> Unit
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
