package com.vocefantasma.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import com.vocefantasma.models.Language
import java.util.Locale

class TtsManager(context: Context) {
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
            }
        }
    }

    fun speak(text: String, language: Language, pitch: Float = 1.0f, rate: Float = 1.0f) {
        if (!isInitialized) return

        val locale = when (language) {
            Language.ITALIAN -> Locale.ITALIAN
            Language.FRENCH -> Locale.FRENCH
        }

        tts?.apply {
            setLanguage(locale)
            setPitch(pitch)
            setSpeechRate(rate)
            speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun stop() {
        tts?.stop()
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
    }
}
