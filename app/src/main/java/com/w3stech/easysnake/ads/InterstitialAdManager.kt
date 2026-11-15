package com.w3stech.easysnake.ads

import android.app.Activity
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.launch

class InterstitialAdManager(private val activity: Activity) {

    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false
    private var lastUnitId: String? = null

    fun load() {
        val scope = (activity as? ComponentActivity)?.lifecycleScope ?: return
        scope.launch { loadInternal(attempt = 0) }
    }

    private suspend fun loadInternal(attempt: Int) {
        if (interstitialAd != null || isLoading) return
        AdsInitializer.awaitReady(activity)
        if (interstitialAd != null || isLoading) return

        isLoading = true
        val unitId = AdIds.nextInterstitialId(previous = lastUnitId)
        lastUnitId = unitId
        val maxAttempts = 5

        InterstitialAd.load(
            activity,
            unitId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.i(TAG, "Interstitial loaded: $unitId")
                    interstitialAd = ad
                    isLoading = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.w(
                        TAG,
                        "Interstitial failed ($unitId): ${error.code} ${error.message}"
                    )
                    interstitialAd = null
                    isLoading = false
                    if (attempt + 1 < maxAttempts) {
                        val scope = (activity as? ComponentActivity)?.lifecycleScope
                        scope?.launch { loadInternal(attempt + 1) }
                    }
                }
            }
        )
    }

    /**
     * Shows a loaded interstitial if available, then invokes [onFinished].
     * Always calls [onFinished] (even when no ad is ready).
     */
    fun show(onFinished: () -> Unit) {
        val ad = interstitialAd
        if (ad == null) {
            Log.w(TAG, "Interstitial not ready; skipping show")
            load()
            onFinished()
            return
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitialAd = null
                load()
                activity.runOnUiThread(onFinished)
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.w(TAG, "Interstitial show failed: ${error.code} ${error.message}")
                interstitialAd = null
                load()
                activity.runOnUiThread(onFinished)
            }
        }
        interstitialAd = null
        ad.show(activity)
    }

    private companion object {
        const val TAG = "EasySnakeAds"
    }
}
