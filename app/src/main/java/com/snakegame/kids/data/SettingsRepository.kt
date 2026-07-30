package com.snakegame.kids.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.snakegame.kids.game.Difficulty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "kids_snake_settings")

data class UserSettings(
    val highScore: Int = 0,
    val difficulty: Difficulty = Difficulty.EASY,
    val soundEnabled: Boolean = true,
    val wrapWalls: Boolean = true
)

class SettingsRepository(private val context: Context) {

    private object Keys {
        val highScore = intPreferencesKey("high_score")
        val difficulty = stringPreferencesKey("difficulty")
        val soundEnabled = booleanPreferencesKey("sound_enabled")
        val wrapWalls = booleanPreferencesKey("wrap_walls")
    }

    val settings: Flow<UserSettings> = context.dataStore.data.map { prefs ->
        UserSettings(
            highScore = prefs[Keys.highScore] ?: 0,
            difficulty = prefs[Keys.difficulty]?.let { name ->
                runCatching { Difficulty.valueOf(name) }.getOrDefault(Difficulty.EASY)
            } ?: Difficulty.EASY,
            soundEnabled = prefs[Keys.soundEnabled] ?: true,
            wrapWalls = prefs[Keys.wrapWalls] ?: true
        )
    }

    suspend fun setDifficulty(difficulty: Difficulty) {
        context.dataStore.edit { it[Keys.difficulty] = difficulty.name }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.soundEnabled] = enabled }
    }

    suspend fun setWrapWalls(enabled: Boolean) {
        context.dataStore.edit { it[Keys.wrapWalls] = enabled }
    }

    suspend fun updateHighScoreIfNeeded(score: Int) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.highScore] ?: 0
            if (score > current) {
                prefs[Keys.highScore] = score
            }
        }
    }
}
