package com.kaelambda.note_grid.screens

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jp.kshoji.javax.sound.midi.MidiEvent
import jp.kshoji.javax.sound.midi.Sequence
import jp.kshoji.javax.sound.midi.ShortMessage
import jp.kshoji.javax.sound.midi.io.StandardMidiFileWriter
import java.io.File
import javax.inject.Inject
import javax.inject.Qualifier

@HiltViewModel
class NoteGridViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var soundController: SoundController

    @NoteGridViewModelModule.OutputFile
    @Inject lateinit var outputFile: File

    @Inject lateinit var mediaPlayer: MediaPlayer

    private val resolution = 960
    private val midiFileWriter = StandardMidiFileWriter()

    fun playSound(scaleDegree: Int) {
        soundController.play(scaleDegree)
    }

    fun generateMidiFile() {
        val sequence = Sequence(Sequence.PPQ, resolution)
        val track = sequence.createTrack()

        for (i in 0L..20L) {
            track.add(MidiEvent(ShortMessage(), resolution * i))
        }

        midiFileWriter.write(sequence, 0, outputFile)

        mediaPlayer.setDataSource(outputFile.toString())
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    override fun onCleared() {
        super.onCleared()
        soundController.release()
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
}
