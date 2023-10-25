# Note Grid

This app is a simple musical toy. It was created for two reasons:
1. To learn about and practice using Jetpack Compose, Google's new UI
   framework for Android.
2. As a proof-of-concept of using MIDI on Android.

---

### Features

Notes are arranged in a grid. The x axis represents time and the y axis
represents pitch. Tap squares on the grid to enable notes, then press "Play"
to play your composition. The available pitches are the natural minor scale,
starting from middle C.

The "Reset" button clears the grid. An arrow to the right of the "Reset"
button will appear if the screen is too small for individual notes to be
tapped accurately, and will allow you to zoom in and out of the grid.

An early implementation of Note Grid used the SoundPool API rather than MIDI.
The switch below the "Play" button toggles between the two implementations.

When MIDI is in use, a soundfont will be loaded from the app's "assets"
folder. The "Instrument" drop-down menu allows any instrument in the soundfont
to be selected for playback.

The "Duration" slider controls the time it takes for the full bar of sixteen
notes to be played.

The "Randomize" button replaces the contents of the grid with random notes.
The text field to the right of the button controls the percentage chance of
each note being enabled.

The "Generate and play MIDI file" button is currently just a test of MIDI
file-writing capability. It writes and plays a simple .mid file containing
a piano playing middle C eight times.

---

### MIDI on Android

The MIDI synthesizer used in this project comes from Sherlock Jiang:
https://github.com/KyoSherlock/MidiDriver

MidiDriver relies in turn on Kaoru Shoji's port of javax.sound.midi
to Android: https://github.com/kshoji/javax.sound.midi-for-Android

Their work made it straightforward to enable MIDI playback using soundfonts.
That will be critical functionality if I go from from this proof-of-concept
to building a MIDI sequencer, so I'm very thankful for both their
contributions to open-source software.