package com.kaelambda.note_grid.audio

fun getNote(scaleDegree: Int): Int {
    return when (scaleDegree) {
        0 -> 60
        1 -> 62
        2 -> 63
        3 -> 65
        4 -> 67
        5 -> 68
        6 -> 70
        7 -> 72
        8 -> 74
        else -> 60
    }
}