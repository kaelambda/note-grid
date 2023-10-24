package com.kaelambda.note_grid.audio

import android.content.Context
import cn.sherlock.com.sun.media.sound.SF2Soundbank
import cn.sherlock.com.sun.media.sound.SoftSynthesizer
import dagger.hilt.android.qualifiers.ApplicationContext
import jp.kshoji.javax.sound.midi.ShortMessage
import javax.inject.Inject

class MidiSoundController @Inject constructor(
    private val synth: SoftSynthesizer,
    @ApplicationContext private val appContext: Context
) {

    init {
//        val sf = SF2Soundbank(appContext.assets.open("OPL-3_FM_128M.sf2"))
        val sf = SF2Soundbank(appContext.assets.open("SmallTimGM6mb.sf2"))

        synth.open()
        synth.loadAllInstruments(sf)
        synth.channels[0].programChange(0)
        synth.channels[1].programChange(1)
    }

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

    fun getCurrentInstrument(): String {
        return synth.loadedInstruments[synth.channels[0].program].name
    }

    fun getAvailableInstruments(): List<String> {
        return synth.loadedInstruments.map { instrument -> instrument.name }
    }

    fun selectInstrument(name: String) {
        synth.channels[0].programChange(
            synth.loadedInstruments.find {
                    instrument -> instrument.name == name
            }?.patch?.program ?: 0
        )
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