package com.kaelambda.note_grid

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * This is required for Hilt integration.
 */
@HiltAndroidApp
class NoteGridApplication : Application()