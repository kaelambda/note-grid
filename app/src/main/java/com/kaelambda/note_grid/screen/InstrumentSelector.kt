package com.kaelambda.note_grid.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kaelambda.note_grid.audio.MidiSoundController

/**
 * There doesn't appear to be a material component for the part of a drop-down menu users see
 * before they open one, so that has been constructed using a box containing Text and Icon elements
 * along with the DropDown.
 *
 * The MidiSoundController is used to fetch and select instruments. It could be better Compose
 * architectural practice to pass the info we're currently fetching (current and available
 * instruments) in as parameters instead of using getters, or to make them available in
 * MidiController as observable properties, but this seems to work just fine.
 */
@Composable
fun InstrumentSelector(midiController: MidiSoundController, enabled: Boolean) {
    var expanded by remember { mutableStateOf(false) }

    Row {
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = "Instrument:"
        )
        Spacer(Modifier.width(16.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable(enabled) { expanded = true },
            color = if (enabled) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.secondary
        ) {
            Box {
                Text(
                    if (enabled) midiController.getCurrentInstrument() else "N/A",
                    color = if (enabled) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .padding(16.dp, 8.dp)
                        .align(Alignment.CenterStart)
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    modifier = Modifier
                        .padding(16.dp, 0.dp)
                        .align(Alignment.CenterEnd),
                    contentDescription = "Localized description"
                )

                val state = rememberLazyListState()
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    // Explicit size is needed for LazyColumn to work within DropdownMenu
                    // Perhaps we could calculate the width though
                    Box(modifier = Modifier.size(width = 200.dp, height = 300.dp)) {
                        LazyColumn(Modifier, state) {
                            items(midiController.getAvailableInstruments()) { instrument ->
                                DropdownMenuItem(
                                    text = { Text(instrument) },
                                    onClick = {
                                        midiController.selectInstrument(instrument)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}