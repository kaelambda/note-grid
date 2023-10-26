package com.kaelambda.note_grid.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Notes are toggleable surfaces that trigger play and stop events when the 'isPlaying' parameter
 * changes. They have a thin border and also set their color based on their state.
 */
@Composable
fun Note(
    noteId: Int,
    isPlaying: Boolean,
    isEnabled: Boolean,
    size: Dp,
    playSound: (Int) -> Unit,
    stopSound: (Int) -> Unit,
    onValueChange: (Boolean) -> Unit
) {
    val color = when {
        isPlaying -> { MaterialTheme.colorScheme.tertiary }
        isEnabled -> { MaterialTheme.colorScheme.secondary }
        else -> { MaterialTheme.colorScheme.secondaryContainer }
    }

    if (isPlaying) {
        playSound(noteId)
    } else {
        stopSound(noteId)
    }

    Surface(
        Modifier
            .size(size)
            .border(2.dp, MaterialTheme.colorScheme.outline)
            .toggleable(
                value = isEnabled,
                enabled = true,
                role = Role.Switch,
                onValueChange = onValueChange
            ),
        color = color
    ) { }
}