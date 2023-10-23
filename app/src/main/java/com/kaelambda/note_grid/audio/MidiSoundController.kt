package com.kaelambda.note_grid.audio

import cn.sherlock.com.sun.media.sound.SoftSynthesizer
import jp.kshoji.javax.sound.midi.ShortMessage
import javax.inject.Inject

class MidiSoundController @Inject constructor() {
    @Inject
    lateinit var synth: SoftSynthesizer
    fun play(scaleDegree: Int) {
        val msg = ShortMessage()
        msg.setMessage(ShortMessage.NOTE_ON, 0, getNote(scaleDegree), 127)
        synth.receiver.send(msg, -1)
    }

    fun stop(scaleDegree: Int) {
        val msg = ShortMessage()
        msg.setMessage(ShortMessage.NOTE_OFF, 0, getNote(scaleDegree), 127)
        synth.receiver.send(msg, -1)
    }

    private fun getNote(scaleDegree: Int): Int {
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
}