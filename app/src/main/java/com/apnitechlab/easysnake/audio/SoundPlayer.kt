package com.apnitechlab.easysnake.audio

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Handler
import android.os.Looper

class SoundPlayer(@Suppress("UNUSED_PARAMETER") context: Context) {
    private var toneGenerator: ToneGenerator? = null
    private val handler = Handler(Looper.getMainLooper())
    private var enabled = true

    init {
        ensureTone()
    }

    fun setEnabled(value: Boolean) {
        enabled = value
    }

    fun playEat() {
        if (!enabled) return
        playTone(ToneGenerator.TONE_PROP_ACK, 90)
    }

    fun playCollision() {
        if (!enabled) return
        playTone(ToneGenerator.TONE_PROP_NACK, 160)
    }

    fun playStart() {
        if (!enabled) return
        playTone(ToneGenerator.TONE_PROP_BEEP, 70)
    }

    fun release() {
        toneGenerator?.release()
        toneGenerator = null
    }

    private fun ensureTone() {
        if (toneGenerator != null) return
        runCatching {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 70)
        }
    }

    private fun playTone(toneType: Int, durationMs: Int) {
        ensureTone()
        val tg = toneGenerator ?: return
        runCatching {
            tg.startTone(toneType, durationMs)
            handler.postDelayed({ runCatching { tg.stopTone() } }, durationMs.toLong() + 20)
        }
    }
}
