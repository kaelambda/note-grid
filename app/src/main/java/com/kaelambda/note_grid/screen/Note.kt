package com.kaelambda.note_grid.screen

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

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
        stopSound(noteId)
    }

    Surface(
        Modifier
            .size(24.dp)
            .toggleable(
                value = isEnabled,
                enabled = true,
                role = Role.Switch,
                onValueChange = onValueChange
            ),
        color = color
    ) { }
}