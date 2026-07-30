package com.snakegame.kids.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.snakegame.kids.game.GameStatus
import com.snakegame.kids.game.GameViewModel
import com.snakegame.kids.ui.screens.GameOverScreen
import com.snakegame.kids.ui.screens.GameScreen
import com.snakegame.kids.ui.screens.HomeScreen
import com.snakegame.kids.ui.screens.SettingsScreen

@Composable
fun KidsSnakeNavHost(
    navController: NavHostController,
    viewModel: GameViewModel
) {
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    LaunchedEffect(gameState.status) {
        if (gameState.status == GameStatus.GameOver) {
            val current = navController.currentDestination?.route
            if (current == Routes.Game) {
                navController.navigate(Routes.GameOver) {
                    popUpTo(Routes.Game) { inclusive = true }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.Home
    ) {
        composable(Routes.Home) {
            HomeScreen(
                highScore = settings.highScore,
                onPlay = {
                    viewModel.prepareNewGame()
                    navController.navigate(Routes.Game)
                },
                onSettings = { navController.navigate(Routes.Settings) }
            )
        }

        composable(Routes.Game) {
            GameScreen(
                state = gameState,
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
                onPlayAgain = {
                    viewModel.prepareNewGame()
                    navController.navigate(Routes.Game) {
                        popUpTo(Routes.Home)
                    }
                },
                onHome = {
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
                onWrapWalls = viewModel::setWrapWalls
            )
        }
    }
}
