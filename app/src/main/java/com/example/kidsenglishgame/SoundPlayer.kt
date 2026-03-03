package com.example.kidsenglishgame

import android.content.Context
import android.media.MediaPlayer

class SoundPlayer(private val context: Context) {

    private var successPlayer: MediaPlayer? = null
    private var failPlayer: MediaPlayer? = null

    fun success() {
        if (successPlayer == null) successPlayer = MediaPlayer.create(context, R.raw.success)
        successPlayer?.start()
    }

    fun fail() {
        if (failPlayer == null) failPlayer = MediaPlayer.create(context, R.raw.fail)
        failPlayer?.start()
    }

    fun release() {
        successPlayer?.release()
        failPlayer?.release()
        successPlayer = null
        failPlayer = null
    }
}
