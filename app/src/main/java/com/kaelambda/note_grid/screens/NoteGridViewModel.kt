package com.kaelambda.note_grid.screens

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteGridViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var soundController: SoundController

    fun playSound(scaleDegree: Int) {
        soundController.play(scaleDegree)
    }

    override fun onCleared() {
        super.onCleared()
        soundController.release()
    }
}