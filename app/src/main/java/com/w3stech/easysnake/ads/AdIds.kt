package com.w3stech.easysnake.ads

import com.w3stech.easysnake.BuildConfig
import kotlin.random.Random

object AdIds {
    val bannerIds: List<String> = BuildConfig.ADMOB_BANNER_IDS.toList()
    val interstitialIds: List<String> = BuildConfig.ADMOB_INTERSTITIAL_IDS.toList()

    /**
     * Picks a random banner unit, preferring a different unit than [previous]
     * when the pool has more than one entry.
     */
    fun nextBannerId(previous: String? = null): String =
        nextId(pool = bannerIds, previous = previous, poolName = "ADMOB_BANNER_IDS")

    /**
     * Picks a random interstitial unit, preferring a different unit than [previous]
     * when the pool has more than one entry.
     */
    fun nextInterstitialId(previous: String? = null): String =
        nextId(pool = interstitialIds, previous = previous, poolName = "ADMOB_INTERSTITIAL_IDS")

    private fun nextId(pool: List<String>, previous: String?, poolName: String): String {
        require(pool.isNotEmpty()) { "$poolName must not be empty" }
        if (pool.size == 1) return pool.first()
        val candidates = if (previous == null) pool else pool.filter { it != previous }
        return candidates.random(Random.Default)
    }
}
