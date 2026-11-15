package com.w3stech.easysnake.ads

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import kotlinx.coroutines.delay

private const val TAG = "EasySnakeAds"
private const val BANNER_REFRESH_MS = 45_000L

@Composable
fun BannerAd(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val adContext = activity ?: context
    val widthDp = LocalConfiguration.current.screenWidthDp
    val lifecycleOwner = LocalLifecycleOwner.current

    var unitId by remember { mutableStateOf(AdIds.nextBannerId()) }
    var refreshToken by remember { mutableIntStateOf(0) }
    var failCount by remember { mutableIntStateOf(0) }
    var bannerHeightDp by remember { mutableIntStateOf(50) }
    var sdkReady by remember { mutableStateOf(false) }
    var adViewRef by remember { mutableStateOf<AdView?>(null) }

    val maxAttempts = AdIds.bannerIds.size.coerceAtLeast(1)

    LaunchedEffect(Unit) {
        AdsInitializer.awaitReady(adContext)
        sdkReady = true
    }

    LaunchedEffect(sdkReady) {
        if (!sdkReady) return@LaunchedEffect
        while (true) {
            delay(BANNER_REFRESH_MS)
            failCount = 0
            unitId = AdIds.nextBannerId(previous = unitId)
            refreshToken++
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            val adView = adViewRef ?: return@LifecycleEventObserver
            when (event) {
                Lifecycle.Event.ON_RESUME -> adView.resume()
                Lifecycle.Event.ON_PAUSE -> adView.pause()
                Lifecycle.Event.ON_DESTROY -> adView.destroy()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            adViewRef?.destroy()
            adViewRef = null
        }
    }

    if (!sdkReady) return

    key(refreshToken) {
        val currentUnitId = unitId

        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .height(bannerHeightDp.dp),
            factory = { ctx ->
                val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(ctx, widthDp)
                bannerHeightDp = adSize.getHeightInPixels(ctx).let { px ->
                    (px / ctx.resources.displayMetrics.density).toInt().coerceAtLeast(50)
                }

                AdView(adContext).apply {
                    setAdSize(adSize)
                    adUnitId = currentUnitId
                    adViewRef = this

                    adListener = object : AdListener() {
                        override fun onAdLoaded() {
                            Log.i(TAG, "Banner loaded: $currentUnitId")
                            failCount = 0
                        }

                        override fun onAdFailedToLoad(error: LoadAdError) {
                            Log.w(
                                TAG,
                                "Banner failed ($currentUnitId): ${error.code} ${error.message}"
                            )
                            if (failCount + 1 >= maxAttempts) {
                                Log.w(TAG, "Banner gave up after $maxAttempts attempts")
                                return
                            }
                            failCount++
                            unitId = AdIds.nextBannerId(previous = currentUnitId)
                            refreshToken++
                        }
                    }

                    loadAd(AdRequest.Builder().build())
                }
            },
            onRelease = { adView ->
                if (adViewRef === adView) adViewRef = null
                adView.destroy()
            }
        )
    }
}

private fun Context.findActivity(): Activity? {
    var current: Context? = this
    while (current is ContextWrapper) {
        if (current is Activity) return current
        current = current.baseContext
    }
    return null
}
