package com.kaelambda.note_grid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kaelambda.note_grid.screen.NoteGridScreen
import com.kaelambda.note_grid.ui.theme.NoteGridTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * The entry-point of our application is MainActivity.onCreate().
 * There, setContent{} is passed our root composables to display.
 * We wrap our NoteGridScreen in the NoteGridTheme so that we can use the colors and other
 * properties selected for our theme.
 *
 * @AndroidEntryPoint tells Hilt to generate dependencies for classes used by this Activity,
 * starting from NoteGridViewModel.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NoteGridTheme {
                NoteGridScreen(viewModel())
            }
        }
    }
}