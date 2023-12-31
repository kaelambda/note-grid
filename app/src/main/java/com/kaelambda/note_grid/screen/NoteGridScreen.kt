package com.kaelambda.note_grid.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.random.Random

const val xCount = 16
const val yCount = 8

/**
 * The root of our view.
 *
 * First, the state of our UI is declared and remembered. This includes a matrix of booleans
 * representing which notes are enabled, and a 'time' Animatable for progressing through the
 * grid when playing.
 *
 * Then, a Surface is populated with our screen's NoteGrid and UI controls. Some UI controls have
 * been refactored into their own components, and we could consider doing that for others to reduce
 * the length of this function.
 */
@Composable
fun NoteGridScreen(viewModel: NoteGridViewModel) {
    var playing by remember { mutableStateOf(false) }
    var randomizationDensity by rememberSaveable { mutableIntStateOf(15) }
    var durationMillis by rememberSaveable { mutableIntStateOf(2500) }
    var zoomedIn by rememberSaveable { mutableStateOf(false) }

    val useMidi by viewModel.useMidi.observeAsState()

    val noteMatrix = rememberSaveable {
        mutableStateOf(
            Array(xCount) {
                Array(yCount) { false }
            }
        )
    }

    val time = remember { Animatable(0f) }
    LaunchedEffect(playing) {
        if (playing) {
            time.animateTo(
                100f,
                animationSpec = infiniteRepeatable(
                    tween(durationMillis, easing = LinearEasing)
                )
            )
        } else {
            time.animateTo(0f, snap())
        }
    }

    Scaffold(
        topBar = {
            Text("NoteGrid by kλ")
        }
    )
    {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                NoteGrid(noteMatrix, time, zoomedIn, viewModel::playSound, viewModel::stopSound)

                Spacer(Modifier.height(16.dp))
                Row {
                    // Play button
                    Button(onClick = {
                        playing = !playing
                    }) {
                        Text(if (playing) "Stop" else "Play")
                    }

                    // Reset button
                    Spacer(Modifier.width(16.dp))
                    Button(onClick = {
                        noteMatrix.value = Array(xCount) { Array(yCount) { false } }
                        playing = false
                    }) {
                        Text("Reset")
                    }

                    // Zoom button
                    val configuration = LocalConfiguration.current
                    val screenWidth = configuration.screenWidthDp.dp
                    val canZoom = screenWidth / xCount < 48.dp
                    if (canZoom) {
                        Box(Modifier.fillMaxWidth()) {
                            IconButton(
                                onClick = { zoomedIn = !zoomedIn },
                                modifier = Modifier.align(Alignment.CenterEnd),
                            ) {
                                Icon(
                                    if (zoomedIn) Icons.Default.KeyboardArrowUp
                                    else Icons.Default.KeyboardArrowDown,
                                    contentDescription = if (zoomedIn) "Zoom out" else "Zoom in"
                                )
                            }
                        }
                    }
                }

                // MIDI / SoundPool switch
                Spacer(Modifier.height(16.dp))
                Row {
                    Switch(
                        checked = useMidi ?: false,
                        onCheckedChange = { checked -> viewModel.setUseMidi(checked) }
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = if (useMidi == true) "Playing with MIDI" else "Playing with SoundPool"
                    )
                }

                Spacer(Modifier.height(16.dp))
                InstrumentSelector(viewModel.midiController, useMidi == true)

                // Duration slider
                Spacer(Modifier.height(16.dp))
                Row {
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = "Duration (ms): $durationMillis"
                    )
                    Spacer(Modifier.width(16.dp))
                    Slider(
                        value = durationMillis.toFloat(),
                        onValueChange = { value -> durationMillis = value.toInt() },
                        valueRange = 500f..5000f,
                        steps = 17
                    )
                }

                // Randomization
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
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = "Density:"
                    )
                    Spacer(Modifier.width(8.dp))
                    TextField(
                        value = if (randomizationDensity > 0) randomizationDensity.toString() else "",
                        onValueChange = { value -> randomizationDensity = value.toIntOrNull() ?: 0 },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.secondary
                        ),
                        textStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimaryContainer)
                    )
                }

                Spacer(Modifier.height(16.dp))
                Button(onClick = { viewModel.saveAndPlayCompositionAsMidiFile(noteMatrix.value) }) {
                    Text("Save and play composition as MIDI file")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    NoteGridScreen(viewModel())
}
