package com.kaelambda.note_grid.screens

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import cn.sherlock.com.sun.media.sound.SF2Soundbank
import cn.sherlock.com.sun.media.sound.SoftSynthesizer
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
    @Inject lateinit var synth: SoftSynthesizer

    private val resolution = 960
    private val midiFileWriter = StandardMidiFileWriter()

    private val useMidi = true

    fun playSound(scaleDegree: Int) {
        if (useMidi) {
            playSoundWithMidi(scaleDegree)
        } else {
            soundController.play(scaleDegree)
        }
    }

    fun stopSound(scaleDegree: Int) {
        if (useMidi) {
            stopMidiSound(scaleDegree)
        }
    }

    private fun playSoundWithMidi(scaleDegree: Int) {
        val msg = ShortMessage()
        msg.setMessage(ShortMessage.NOTE_ON, 0, 60 - scaleDegree, 127)
        synth.receiver.send(msg, -1)
    }

    private fun stopMidiSound(scaleDegree: Int) {
        val msg = ShortMessage()
        msg.setMessage(ShortMessage.NOTE_OFF, 0, 60 - scaleDegree, 127)
        synth.receiver.send(msg, -1)
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
