package com.apnitechlab.easysnake

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
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.apnitechlab.easysnake.ads.InterstitialAdManager
import com.apnitechlab.easysnake.game.GameViewModel
import com.apnitechlab.easysnake.ui.navigation.EasySnakeNavHost
import com.apnitechlab.easysnake.ui.theme.EasySnakeTheme
import com.apnitechlab.easysnake.ui.theme.SoftCream

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Kids-friendly ad settings (COPPA / Families)
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTagForChildDirectedTreatment(
                    RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE
                )
                .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_G)
                .build()
        )
        MobileAds.initialize(this) {}

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
