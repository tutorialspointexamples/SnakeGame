package com.w3stech.easysnake

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.w3stech.easysnake.ads.AdsInitializer
import com.w3stech.easysnake.ads.InterstitialAdManager
import com.w3stech.easysnake.game.GameViewModel
import com.w3stech.easysnake.ui.navigation.EasySnakeNavHost
import com.w3stech.easysnake.ui.theme.EasySnakeTheme
import com.w3stech.easysnake.ui.theme.SoftCream

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        AdsInitializer.initialize(this)

        setContent {
            EasySnakeTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                    color = SoftCream
                ) {
                    val navController = rememberNavController()
                    val interstitialAds = remember { InterstitialAdManager(this@MainActivity) }
                    EasySnakeNavHost(
                        navController = navController,
                        viewModel = viewModel,
                        interstitialAds = interstitialAds
                    )
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }
}
