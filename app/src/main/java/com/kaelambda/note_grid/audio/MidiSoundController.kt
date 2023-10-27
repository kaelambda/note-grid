package com.kaelambda.note_grid.audio

import android.content.Context
import cn.sherlock.com.sun.media.sound.SF2Soundbank
import cn.sherlock.com.sun.media.sound.SoftSynthesizer
import dagger.hilt.android.qualifiers.ApplicationContext
import jp.kshoji.javax.sound.midi.ShortMessage
import javax.inject.Inject

/**
 * Playback controller using MIDI. This relies on SoftSynthesizer and the MidiDriver library.
 *
 * On initialize, an SF2Soundbank is loaded. You can replace the one included with your own by
 * placing a different .sf2 file in the 'assets' folder, and change the fileName passed into
 * appContext.assets.open().
 *
 * This class sends NOTE_ON and NOTE_OFF messages to the synth and provides functions to get and
 * set the current instrument, and to get the list of all available instruments.
 */
class MidiSoundController @Inject constructor(
    private val synth: SoftSynthesizer,
    @ApplicationContext private val appContext: Context
) {

    init {
//        val soundbank = SF2Soundbank(appContext.assets.open("OPL-3_FM_128M.sf2"))
        val soundbank = SF2Soundbank(appContext.assets.open("SmallTimGM6mb.sf2"))

        synth.open()
        synth.loadAllInstruments(soundbank)
        synth.channels[0].programChange(0)
    }

    fun close() {
        synth.close()
    }

    fun play(scaleDegree: Int) {
        val message = ShortMessage()
        message.setMessage(ShortMessage.NOTE_ON, 0, getNote(scaleDegree), 127)
        synth.receiver.send(message, -1)
    }

    fun stop(scaleDegree: Int) {
        val message = ShortMessage()
        message.setMessage(ShortMessage.NOTE_OFF, 0, getNote(scaleDegree), 127)
        synth.receiver.send(message, -1)
    }

    fun getCurrentInstrument(): String {
        return synth.loadedInstruments[synth.channels[0].program].name
    }

    fun getCurrentInstrumentId(): Int {
        return synth.channels[0].program
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
}