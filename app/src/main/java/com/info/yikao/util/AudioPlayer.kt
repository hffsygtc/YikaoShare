package com.info.yikao.util
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import java.io.IOException

class AudioPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun startPlaying() {
        mediaPlayer = MediaPlayer()
        try {
            val assetManager = context.assets
            val fileDescriptor: AssetFileDescriptor =
                assetManager.openFd("notice.aac")
            mediaPlayer?.setDataSource(
                fileDescriptor.fileDescriptor,
                fileDescriptor.startOffset,
                fileDescriptor.length
            )
            mediaPlayer?.prepare()
            mediaPlayer?.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun stopPlaying() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}