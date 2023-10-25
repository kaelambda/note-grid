package com.kaelambda.note_grid.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
                    modifier = Modifier.padding(16.dp, 8.dp).align(Alignment.CenterStart)
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    modifier = Modifier.padding(16.dp, 0.dp).align(Alignment.CenterEnd),
                    contentDescription = "Localized description"
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    for (instrument in midiController.getAvailableInstruments()) {
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