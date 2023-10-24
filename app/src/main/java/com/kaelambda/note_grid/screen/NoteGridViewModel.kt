package com.kaelambda.note_grid.screen

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    private val _useMidi = MutableLiveData(true)
    val useMidi: LiveData<Boolean> = _useMidi

    fun playSound(scaleDegree: Int) {
        if (useMidi.value == true) {
            midiController.play(scaleDegree)
        } else {
            soundPoolController.play(scaleDegree)
        }
    }

    fun stopSound(scaleDegree: Int) {
        if (useMidi.value == true) {
            midiController.stop(scaleDegree)
        }
    }

    fun setUseMidi(value: Boolean) {
        _useMidi.value = value
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
    fun providesSynth(): SoftSynthesizer {
        return SoftSynthesizer()
    }
}
