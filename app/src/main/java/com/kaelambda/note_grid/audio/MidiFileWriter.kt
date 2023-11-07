package com.kaelambda.note_grid.audio

import com.kaelambda.note_grid.screen.NoteGridViewModelModule
import com.kaelambda.note_grid.screen.xCount
import jp.kshoji.javax.sound.midi.MidiEvent
import jp.kshoji.javax.sound.midi.Sequence
import jp.kshoji.javax.sound.midi.ShortMessage
import jp.kshoji.javax.sound.midi.io.StandardMidiFileWriter
import java.io.File
import javax.inject.Inject

private const val RESOLUTION = 24
private const val EIGHTH_NOTE = RESOLUTION / 2

/**
 * Utility for writing MIDI data representing the NoteGrid to a file
 */
class MidiFileWriter @Inject constructor(
    @NoteGridViewModelModule.OutputFile private val outputFile: File
) {
    private val midiFileWriter = StandardMidiFileWriter()

    fun writeCompositionToMidiFile(noteGrid: Array<Array<Boolean>>, instrument: Int): File {
        val sequence = Sequence(Sequence.PPQ, RESOLUTION)
        val track = sequence.createTrack()

        track.add(
            MidiEvent(ShortMessage(ShortMessage.PROGRAM_CHANGE, instrument, 0), 0L)
        )

        for ((x, column) in noteGrid.withIndex()) {
            val notePosition = EIGHTH_NOTE * x.toLong()
            for ((y, enabled) in column.withIndex()) {
                if (enabled) {
                    val onMessage = ShortMessage(ShortMessage.NOTE_ON, 0, getNote(7 - y), 127)
                    track.add(MidiEvent(onMessage, notePosition))
                }
            }
            for ((y, _) in column.withIndex()) {
                val offMessage = ShortMessage(ShortMessage.NOTE_OFF, 0, getNote(7 - y), 127)
                track.add(MidiEvent(offMessage, notePosition + EIGHTH_NOTE))
            }
        }

        track.add(
            MidiEvent(ShortMessage(ShortMessage.STOP), EIGHTH_NOTE * xCount.toLong() + RESOLUTION)
        )

        midiFileWriter.write(sequence, 0, outputFile)
        return outputFile
    }
}