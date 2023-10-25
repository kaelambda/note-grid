package com.kaelambda.note_grid.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun NoteGrid(
    noteMatrix: MutableState<Array<Array<Boolean>>>,
    t: Animatable<Float, AnimationVector1D>,
    zoomedIn: Boolean,
    playSound: (Int) -> Unit,
    stopSound: (Int) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    var noteSize by remember { mutableStateOf(46.dp) }
    noteSize = if (!zoomedIn || screenWidth / xCount > 46.dp)
        (screenWidth / xCount) - 2.dp
    else
        48.dp

    Column(Modifier.horizontalScroll(rememberScrollState())) {
        for (y in 0 until yCount) {
            Row {
                for (x in 0 until xCount) {
                    val startTime = duration * x
                    val endTime = startTime + duration

                    val isEnabled = noteMatrix.value[x][y]
                    val isPlaying = isEnabled.and(t.value > startTime && t.value < endTime)

                    Note(
                        7 - y,
                        isPlaying,
                        isEnabled,
                        noteSize,
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