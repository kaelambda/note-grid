package com.kaelambda.note_grid

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kaelambda.note_grid.screens.NoteGridScreen
import com.kaelambda.note_grid.ui.theme.NoteGridTheme

val soundPool: SoundPool = SoundPool.Builder()
    .setMaxStreams(8)
    .setAudioAttributes(
        AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build())
    .build()
val soundPoolIds = mutableListOf<Int>()

class MainActivity : ComponentActivity() {
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        for (id in staccatoSynthIds) {
            soundPoolIds.add(soundPool.load(applicationContext, id, 1))
        }

        setContent {
            NoteGridTheme {
                NoteGridScreen()
            }
        }
    }
}