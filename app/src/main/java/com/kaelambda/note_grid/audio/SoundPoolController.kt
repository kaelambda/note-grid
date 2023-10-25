package com.kaelambda.note_grid.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.kaelambda.note_grid.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SoundPoolController @Inject constructor(@ApplicationContext context: Context) {
    private val synthIds = listOf(
        R.raw.triangle_synth_1_c,
        R.raw.triangle_synth_2_d,
        R.raw.triangle_synth_3_dsharp,
        R.raw.triangle_synth_4_f,
        R.raw.triangle_synth_5_g,
        R.raw.triangle_synth_6_gsharp,
        R.raw.triangle_synth_7_asharp,
        R.raw.triangle_synth_8_highc,
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
        for (id in synthIds) {
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