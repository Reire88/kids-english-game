package com.example.kidsenglishgame

import android.content.Context
import android.media.MediaPlayer

class SoundPlayer(private val context: Context) {

    private var successPlayer: MediaPlayer? = null
    private var failPlayer: MediaPlayer? = null

    fun success() {
        try {
            successPlayer?.release()
            successPlayer = MediaPlayer.create(context, R.raw.success)
            successPlayer?.start()
        } catch (_: Exception) {
        }
    }

    fun fail() {
        try {
            failPlayer?.release()
            failPlayer = MediaPlayer.create(context, R.raw.fail)
            failPlayer?.start()
        } catch (_: Exception) {
        }
    }

    fun release() {
        try {
            successPlayer?.release()
            failPlayer?.release()
        } catch (_: Exception) {
        }
        successPlayer = null
        failPlayer = null
    }
}
