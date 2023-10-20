package com.kaelambda.note_grid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kaelambda.note_grid.screens.NoteGridScreen
import com.kaelambda.note_grid.ui.theme.NoteGridTheme
import dagger.hilt.android.AndroidEntryPoint

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