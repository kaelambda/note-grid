package com.kaelambda.note_grid.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember

@Composable
fun NoteGrid(
    noteMatrix: MutableState<Array<Array<Boolean>>>,
    t: Animatable<Float, AnimationVector1D>,
    playSound: (Int) -> Unit,
    stopSound: (Int) -> Unit,
) {
    Column {
        for (y in 0 until yCount) {
            Row {
                for (x in 0 until xCount) {
                    val startTime = remember { duration * x }
                    val endTime = remember { startTime + duration }

                    val isEnabled = noteMatrix.value[x][y]
                    val isPlaying = isEnabled.and(t.value > startTime && t.value < endTime)

                    Note(
                        y,
                        isPlaying,
                        isEnabled,
                        playSound,
                        stopSound
                    ) {
                        noteMatrix.value = noteMatrix.value.clone().apply {
                            this[x][y] = it
                        }
                    }
                }
            }
        }
    }
}