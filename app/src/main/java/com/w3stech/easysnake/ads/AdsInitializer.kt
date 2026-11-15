package com.w3stech.easysnake.ads

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import kotlinx.coroutines.CompletableDeferred

object AdsInitializer {
    private const val TAG = "EasySnakeAds"

    @Volatile
    private var ready: CompletableDeferred<Unit>? = null

    fun initialize(context: Context) {
        val appContext = context.applicationContext
        synchronized(this) {
            if (ready != null) return
            ready = CompletableDeferred()
        }

        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_PG)
                .build()
        )

        MobileAds.initialize(appContext) { status ->
            Log.i(TAG, "MobileAds initialized: ${status.adapterStatusMap.keys}")
            ready?.complete(Unit)
        }
    }

    suspend fun awaitReady(context: Context) {
        initialize(context)
        val deferred = synchronized(this) { ready }
            ?: error("AdsInitializer ready signal missing after initialize()")
        deferred.await()
    }
}
