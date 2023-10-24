package com.kaelambda.note_grid.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun InstrumentSelector(midiController: MidiSoundController) {
    var expanded by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable { expanded = true },
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Box {
            Text(
                midiController.getCurrentInstrument(),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
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