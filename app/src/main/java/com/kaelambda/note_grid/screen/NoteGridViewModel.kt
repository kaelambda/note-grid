package com.kaelambda.note_grid.screen

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import cn.sherlock.com.sun.media.sound.SF2Soundbank
import cn.sherlock.com.sun.media.sound.SoftSynthesizer
import com.kaelambda.note_grid.audio.MidiFileWriter
import com.kaelambda.note_grid.audio.MidiSoundController
import com.kaelambda.note_grid.audio.SoundPoolController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Qualifier

@HiltViewModel
class NoteGridViewModel @Inject constructor() : ViewModel() {
    @Inject lateinit var soundPoolController: SoundPoolController
    @Inject lateinit var midiController: MidiSoundController
    @Inject lateinit var midiFileWriter: MidiFileWriter
    @Inject lateinit var mediaPlayer: MediaPlayer

    private val useMidi = true

    fun playSound(scaleDegree: Int) {
        if (useMidi) {
            midiController.play(scaleDegree)
        } else {
            soundPoolController.play(scaleDegree)
        }
    }

    fun stopSound(scaleDegree: Int) {
        if (useMidi) {
            midiController.stop(scaleDegree)
        }
    }

    fun generateMidiFile() {
        mediaPlayer.setDataSource(
            midiFileWriter.generateMidiFile().toString()
        )
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    override fun onCleared() {
        super.onCleared()
        soundPoolController.release()
    }
}

@Module
@InstallIn(ViewModelComponent::class)
object NoteGridViewModelModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OutputFile

    @OutputFile
    @Provides
    fun provideOutputFile(@ApplicationContext appContext: Context) : File {
        val outputFile = File(appContext.filesDir, "exampleout.mid")
        outputFile.createNewFile()
        return outputFile
    }

    @Provides
    fun provideMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }

    @Provides
    fun providesSynth(@ApplicationContext appContext: Context): SoftSynthesizer {
//        val sf = SF2Soundbank(appContext.assets.open("OPL-3_FM_128M.sf2"))
        val sf = SF2Soundbank(appContext.assets.open("SmallTimGM6mb.sf2"))

        val synth = SoftSynthesizer()
        synth.open()
        synth.loadAllInstruments(sf)
        synth.channels[0].programChange(0)
        synth.channels[1].programChange(1)

        return synth
    }
}
