package com.apnitechlab.easysnake.ads

import android.app.Activity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class InterstitialAdManager(private val activity: Activity) {

    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false

    fun load() {
        if (interstitialAd != null || isLoading) return
        isLoading = true
        InterstitialAd.load(
            activity,
            AdIds.interstitial,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoading = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    isLoading = false
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
                interstitialAd = null
                load()
                activity.runOnUiThread(onFinished)
            }
        }
        interstitialAd = null
        ad.show(activity)
    }
}
