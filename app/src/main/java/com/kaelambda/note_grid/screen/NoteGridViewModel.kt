package com.kaelambda.note_grid.screen

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kaelambda.note_grid.audio.MidiFileWriter
import com.kaelambda.note_grid.audio.MidiPlaybackController
import com.kaelambda.note_grid.audio.SoundPoolPlaybackController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Our screen's ViewModel contains references to various controllers of business logic, a
 * MediaPlayer, and functions for responding to events triggered by the UI.
 *
 * The 'useMidi' state variable is hoisted here so that it can be used in the ViewModel's logic.
 */
@HiltViewModel
class NoteGridViewModel @Inject constructor() : ViewModel() {
    @Inject lateinit var soundPoolController: SoundPoolPlaybackController
    @Inject lateinit var midiController: MidiPlaybackController
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

    fun saveAndPlayCompositionAsMidiFile(noteGrid: Array<Array<Boolean>>) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(
            midiFileWriter
                .writeCompositionToMidiFile(noteGrid, midiController.getCurrentInstrumentId())
                .toString()
        )
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    override fun onCleared() {
        super.onCleared()
        soundPoolController.release()
        midiController.close()
    }
}
