package com.kaelambda.note_grid.screen

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var randomizationDensity by remember { mutableIntStateOf(15) }
    var durationMillis by remember { mutableIntStateOf(2500) }

    val useMidi by viewModel.useMidi.observeAsState()

    val noteMatrix = remember {
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
                    tween(durationMillis, easing = LinearEasing)
                )
            )
        } else {
            t.animateTo(0f, snap())
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            NoteGrid(noteMatrix, t, viewModel::playSound, viewModel::stopSound)

            Spacer(Modifier.height(16.dp))
            Row {
                Button(onClick = {
                    playing = !playing
                }) {
                    Text(if (playing) "Stop" else "Play")
                }

                Spacer(Modifier.width(16.dp))
                Button(onClick = {
                    noteMatrix.value = Array(xCount) { Array(yCount) { false } }
                    playing = false
                }) {
                    Text("Reset")
                }
            }

            Spacer(Modifier.height(16.dp))
            Row {
                Switch(
                    checked = useMidi ?: false,
                    onCheckedChange = { viewModel.setUseMidi(it) }
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = if (useMidi == true) "Playing with MIDI" else "Playing with SoundPool"
                )
            }

            Spacer(Modifier.height(16.dp))
            Row {
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = "Instrument:"
                )
                Spacer(Modifier.width(16.dp))
                InstrumentSelector(viewModel.midiController)
            }

            Spacer(Modifier.height(16.dp))
            Row {
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = "Duration (in milliseconds): $durationMillis"
                )
                Spacer(Modifier.width(16.dp))
                Slider(
                    value = durationMillis.toFloat(),
                    onValueChange = { durationMillis = it.toInt() },
                    valueRange = 500f .. 10000f
                )
            }
            
            Spacer(Modifier.height(16.dp))
            Row {
                Button(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = {
                        noteMatrix.value = Array(xCount) {
                            Array(yCount) { Random.nextInt(100) < randomizationDensity }
                    }
                    playing = false
                }) {
                    Text("Randomize")
                }

                Spacer(Modifier.width(16.dp))
                TextField(
                    value = if (randomizationDensity > 0) randomizationDensity.toString() else "",
                    onValueChange = { randomizationDensity = it.toIntOrNull() ?: 0 },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(Modifier.height(16.dp))
            Button(onClick = viewModel::generateMidiFile) {
                Text("Generate and play MIDI file")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    NoteGridScreen(viewModel())
}
