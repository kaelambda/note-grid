package com.kaelambda.note_grid.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.random.Random

const val xCount = 16
const val yCount = 8
const val duration = 100f / xCount.toFloat()

@Composable
fun NoteGridScreen(viewModel: NoteGridViewModel) {
    var playing by remember { mutableStateOf(false) }
    var buttonText by remember { mutableStateOf("Play") }
    var randomizationDensity by remember { mutableIntStateOf(25) }

    var noteMatrix by remember {
        mutableStateOf(
            Array(xCount) {
                Array(yCount) { false }
            }
        )
    }

    val t = remember { Animatable(0f) }
    LaunchedEffect(playing) {
        if (playing) {
            t.animateTo(
                100f,
                animationSpec = infiniteRepeatable(
                    tween(2500, easing = LinearEasing)
                )
            )
        } else {
            t.animateTo(0f, snap())
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            for (y in 0 until yCount) {
                Row {
                    for (x in 0 until xCount) {
                        val startTime = remember { duration * x }
                        val endTime = remember { startTime + duration }

                        val isEnabled = noteMatrix[x][y]
                        val isPlaying = isEnabled.and(t.value > startTime && t.value < endTime)

                        Note(
                            y,
                            isPlaying,
                            isEnabled,
                            viewModel::playSound,
                            viewModel::stopSound
                        ) {
                            noteMatrix = noteMatrix.clone().apply {
                                this[x][y] = it
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row {
                Button(onClick = {
                    playing = !playing
                    buttonText = if (playing) "Stop" else "Play"
                }) {
                    Text(buttonText)
                }

                Spacer(Modifier.width(16.dp))
                Button(onClick = {
                    noteMatrix = Array(xCount) { Array(yCount) { false } }
                }) {
                    Text("Reset")
                }

                Spacer(Modifier.width(16.dp))
                Button(onClick = {
                    noteMatrix = Array(xCount) { Array(yCount) { Random.nextInt(100) < randomizationDensity } }
                }) {
                    Text("Randomize")
                }
            }

            Spacer(Modifier.height(16.dp))
            TextField(value = if (randomizationDensity > 0) randomizationDensity.toString() else "",
                onValueChange = { randomizationDensity = it.toIntOrNull() ?: 0 },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(Modifier.height(16.dp))
            Button(onClick = viewModel::generateMidiFile) {
                Text("Generate and play MIDI file")
            }
        }
    }
}

@Composable
fun Note(
    noteId: Int,
    isPlaying: Boolean,
    isEnabled: Boolean,
    playSound: (Int) -> Unit,
    stopSound: (Int) -> Unit,
    onValueChange: (Boolean) -> Unit
) {
    val color = when {
        isPlaying -> { Color.Green }
        isEnabled -> { Color.Red }
        else -> { Color.Gray }
    }

    if (isPlaying) {
        playSound(noteId)
    } else {
        // This will trigger for notes on the grid that are disabled or not currently playing
        // but with the same noteId if notes are being recomposed when they shouldn't be

        // Yes -- seems every note is being recomposed when e.g. I enable a single note.
        // Could this be because isPlaying and isEnabled are vals that are recalculated every
        // time the screen is recomposed?

        // Perhaps we should hoist the state into a StateHolder for each Note?
        // Or use remember / mutableStateOf / derivedStateOf in the proper way?

        // When NoteMatrix changes, could that have knockon effects?
        // A breakpoint here is also hit for every note as t changes while playing

        stopSound(noteId)
    }

    Surface(
        Modifier
            .size(20.dp)
            .toggleable(
                value = isEnabled,
                enabled = true,
                role = Role.Switch,
                onValueChange = onValueChange
            ),
        color = color
    ) { }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    NoteGridScreen(viewModel())
}
