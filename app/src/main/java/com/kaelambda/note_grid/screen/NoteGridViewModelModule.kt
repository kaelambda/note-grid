package com.kaelambda.note_grid.screen

import android.content.Context
import android.media.MediaPlayer
import cn.sherlock.com.sun.media.sound.SoftSynthesizer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Qualifier

/**
 * Hilt module for providing dependencies to ViewModel and its object graph.
 */
@Module
@InstallIn(ViewModelComponent::class)
object NoteGridViewModelModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OutputFile

    @OutputFile
    @Provides
    fun provideOutputFile(@ApplicationContext appContext: Context) : File {
        val outputFile = File(appContext.filesDir, "exampleout.mid")
        outputFile.createNewFile()
        return outputFile
    }

    @Provides
    fun provideMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }

    @Provides
    fun providesSynth(): SoftSynthesizer {
        return SoftSynthesizer()
    }
}