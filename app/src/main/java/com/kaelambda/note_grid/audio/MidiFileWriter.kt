package com.kaelambda.note_grid.audio

import com.kaelambda.note_grid.screen.NoteGridViewModelModule
import jp.kshoji.javax.sound.midi.MidiEvent
import jp.kshoji.javax.sound.midi.Sequence
import jp.kshoji.javax.sound.midi.ShortMessage
import jp.kshoji.javax.sound.midi.io.StandardMidiFileWriter
import java.io.File
import javax.inject.Inject

private const val RESOLUTION = 960
class MidiFileWriter @Inject constructor() {

    @NoteGridViewModelModule.OutputFile
    @Inject lateinit var outputFile: File

    private val midiFileWriter = StandardMidiFileWriter()
    fun generateMidiFile(): File {
        val sequence = Sequence(Sequence.PPQ, RESOLUTION)
        val track = sequence.createTrack()

        for (i in 0L..20L) {
            track.add(MidiEvent(ShortMessage(), RESOLUTION * i))
        }

        midiFileWriter.write(sequence, 0, outputFile)
        return outputFile
    }
}