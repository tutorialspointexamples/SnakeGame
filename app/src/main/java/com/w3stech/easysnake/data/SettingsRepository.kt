package com.w3stech.easysnake.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.w3stech.easysnake.game.Difficulty
import com.w3stech.easysnake.game.SnakeSkin
import com.w3stech.easysnake.game.StickerId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "easy_snake_settings")

data class UserSettings(
    val highScore: Int = 0,
    val difficulty: Difficulty = Difficulty.EASY,
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val wrapWalls: Boolean = true,
    val gamesPlayed: Int = 0,
    val unlockedStickers: Set<String> = emptySet(),
    val selectedSkinId: String = SnakeSkin.CLASSIC.id
) {
    val selectedSkin: SnakeSkin
        get() {
            val skin = SnakeSkin.fromId(selectedSkinId)
            return if (skin.isUnlocked(unlockedStickers)) skin else SnakeSkin.CLASSIC
        }
}

class SettingsRepository(private val context: Context) {

    private object Keys {
        val highScore = intPreferencesKey("high_score")
        val difficulty = stringPreferencesKey("difficulty")
        val soundEnabled = booleanPreferencesKey("sound_enabled")
        val hapticsEnabled = booleanPreferencesKey("haptics_enabled")
        val wrapWalls = booleanPreferencesKey("wrap_walls")
        val gamesPlayed = intPreferencesKey("games_played")
        val unlockedStickers = stringSetPreferencesKey("unlocked_stickers")
        val selectedSkin = stringPreferencesKey("selected_skin")
    }

    val settings: Flow<UserSettings> = context.dataStore.data.map { prefs ->
        UserSettings(
            highScore = prefs[Keys.highScore] ?: 0,
            difficulty = prefs[Keys.difficulty]?.let { name ->
                runCatching { Difficulty.valueOf(name) }.getOrDefault(Difficulty.EASY)
            } ?: Difficulty.EASY,
            soundEnabled = prefs[Keys.soundEnabled] ?: true,
            hapticsEnabled = prefs[Keys.hapticsEnabled] ?: true,
            wrapWalls = prefs[Keys.wrapWalls] ?: true,
            gamesPlayed = prefs[Keys.gamesPlayed] ?: 0,
            unlockedStickers = prefs[Keys.unlockedStickers] ?: emptySet(),
            selectedSkinId = prefs[Keys.selectedSkin] ?: SnakeSkin.CLASSIC.id
        )
    }

    suspend fun setDifficulty(difficulty: Difficulty) {
        context.dataStore.edit { it[Keys.difficulty] = difficulty.name }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.soundEnabled] = enabled }
    }

    suspend fun setHapticsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.hapticsEnabled] = enabled }
    }

    suspend fun setWrapWalls(enabled: Boolean) {
        context.dataStore.edit { it[Keys.wrapWalls] = enabled }
    }

    suspend fun setSelectedSkin(skin: SnakeSkin) {
        context.dataStore.edit { prefs ->
            val unlocked = prefs[Keys.unlockedStickers] ?: emptySet()
            if (skin.isUnlocked(unlocked)) {
                prefs[Keys.selectedSkin] = skin.id
            }
        }
    }

    suspend fun updateHighScoreIfNeeded(score: Int) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.highScore] ?: 0
            if (score > current) {
                prefs[Keys.highScore] = score
            }
        }
    }

    /**
     * Records end-of-run stats and unlocks any new stickers.
     * @return newly unlocked sticker ids (for UI celebration if desired)
     */
    suspend fun recordGameFinished(score: Int, won: Boolean): Set<String> {
        var newlyUnlocked = emptySet<String>()
        context.dataStore.edit { prefs ->
            val currentHigh = prefs[Keys.highScore] ?: 0
            if (score > currentHigh) {
                prefs[Keys.highScore] = score
            }

            val games = (prefs[Keys.gamesPlayed] ?: 0) + 1
            prefs[Keys.gamesPlayed] = games

            val unlocked = (prefs[Keys.unlockedStickers] ?: emptySet()).toMutableSet()
            val before = unlocked.toSet()

            if (score >= 1) unlocked.add(StickerId.FIRST_FRUIT)
            if (score >= 10) unlocked.add(StickerId.SCORE_10)
            if (score >= 25) unlocked.add(StickerId.SCORE_25)
            if (score >= 50) unlocked.add(StickerId.SCORE_50)
            if (games >= 5) unlocked.add(StickerId.PLAY_5)
            if (won) unlocked.add(StickerId.FILL_BOARD)

            prefs[Keys.unlockedStickers] = unlocked
            newlyUnlocked = unlocked - before
        }
        return newlyUnlocked
    }
}
