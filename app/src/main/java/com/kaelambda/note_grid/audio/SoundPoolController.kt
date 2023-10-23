package com.kaelambda.note_grid.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.kaelambda.note_grid.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SoundPoolController @Inject constructor(@ApplicationContext context: Context) {
    private val staccatoSynthIds = listOf(
        R.raw.staccatosynth1,
        R.raw.staccatosynth2,
        R.raw.staccatosynth3,
        R.raw.staccatosynth4,
        R.raw.staccatosynth5,
        R.raw.staccatosynth6,
        R.raw.staccatosynth7,
        R.raw.staccatosynth8,
    )

    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(8)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build())
        .build()

    private val soundPoolIds = mutableListOf<Int>()

    init {
        for (id in staccatoSynthIds) {
            soundPoolIds.add(soundPool.load(context, id, 1))
        }
    }

    fun play(scaleDegree: Int): Int {
        return soundPool.play(soundPoolIds[scaleDegree], 1f, 1f, 1, 0, 1f)
    }

    fun release() {
        soundPool.release()
    }
}