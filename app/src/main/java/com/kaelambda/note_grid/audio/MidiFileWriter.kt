package com.kaelambda.note_grid.audio

import com.kaelambda.note_grid.screen.NoteGridViewModelModule
import jp.kshoji.javax.sound.midi.MidiEvent
import jp.kshoji.javax.sound.midi.Sequence
import jp.kshoji.javax.sound.midi.ShortMessage
import jp.kshoji.javax.sound.midi.io.StandardMidiFileWriter
import java.io.File
import javax.inject.Inject

private const val RESOLUTION = 960

/**
 * Utility for writing MIDI data to a file.
 *
 * This is currently just a test of MIDI file-writing capability. It writes and plays a simple .mid
 * file containing a piano playing middle C eight times.
 *
 * If the ability to save the current NoteGrid contents as a MIDI file were to be added, that
 * function would go here.
 */
class MidiFileWriter @Inject constructor(
    @NoteGridViewModelModule.OutputFile private val outputFile: File
) {
    private val midiFileWriter = StandardMidiFileWriter()
    fun generateMidiFile(): File {
        val sequence = Sequence(Sequence.PPQ, RESOLUTION)
        val track = sequence.createTrack()

        for (i in 0L..8L) {
            track.add(MidiEvent(ShortMessage(), RESOLUTION * i))
        }

        midiFileWriter.write(sequence, 0, outputFile)
        return outputFile
    }
}