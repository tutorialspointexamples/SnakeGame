package com.w3stech.easysnake.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.w3stech.easysnake.ads.InterstitialAdManager
import com.w3stech.easysnake.game.GameStatus
import com.w3stech.easysnake.game.GameViewModel
import com.w3stech.easysnake.ui.screens.AchievementsScreen
import com.w3stech.easysnake.ui.screens.GameOverScreen
import com.w3stech.easysnake.ui.screens.GameScreen
import com.w3stech.easysnake.ui.screens.HomeScreen
import com.w3stech.easysnake.ui.screens.SettingsScreen

@Composable
fun EasySnakeNavHost(
    navController: NavHostController,
    viewModel: GameViewModel,
    interstitialAds: InterstitialAdManager
) {
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val newlyUnlocked by viewModel.newlyUnlocked.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        interstitialAds.load()
    }

    LaunchedEffect(gameState.status) {
        val ended = gameState.status == GameStatus.GameOver || gameState.status == GameStatus.Won
        if (ended) {
            val current = navController.currentDestination?.route
            if (current == Routes.Game) {
                interstitialAds.show {
                    navController.navigate(Routes.GameOver) {
                        popUpTo(Routes.Game) { inclusive = true }
                    }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.Home,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() }
    ) {
        composable(Routes.Home) {
            HomeScreen(
                highScore = settings.highScore,
                stickerCount = settings.unlockedStickers.size,
                skin = settings.selectedSkin,
                onPlay = {
                    viewModel.prepareNewGame()
                    navController.navigate(Routes.Game)
                },
                onSettings = { navController.navigate(Routes.Settings) },
                onAchievements = { navController.navigate(Routes.Achievements) }
            )
        }

        composable(Routes.Game) {
            GameScreen(
                state = gameState,
                skin = settings.selectedSkin,
                hapticsEnabled = settings.hapticsEnabled,
                feedback = viewModel.feedback,
                onDirection = viewModel::onDirection,
                onPause = viewModel::pause,
                onResume = viewModel::startOrResume,
                onHome = {
                    viewModel.prepareNewGame()
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Home) { inclusive = true }
                    }
                },
                onStart = viewModel::startOrResume
            )
        }

        composable(Routes.GameOver) {
            GameOverScreen(
                score = gameState.score,
                highScore = settings.highScore,
                won = gameState.status == GameStatus.Won,
                newlyUnlocked = newlyUnlocked,
                onPlayAgain = {
                    viewModel.clearNewlyUnlocked()
                    viewModel.prepareNewGame()
                    navController.navigate(Routes.Game) {
                        popUpTo(Routes.Home)
                    }
                },
                onHome = {
                    viewModel.clearNewlyUnlocked()
                    viewModel.prepareNewGame()
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Home) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Settings) {
            SettingsScreen(
                settings = settings,
                onBack = { navController.popBackStack() },
                onDifficulty = viewModel::setDifficulty,
                onSound = viewModel::setSoundEnabled,
                onHaptics = viewModel::setHapticsEnabled,
                onWrapWalls = viewModel::setWrapWalls,
                onSelectSkin = viewModel::setSelectedSkin
            )
        }

        composable(Routes.Achievements) {
            AchievementsScreen(
                unlockedStickers = settings.unlockedStickers,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
