package com.vocefantasma.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import com.vocefantasma.models.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class TtsManager(context: Context) {
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _availableVoices = MutableStateFlow<List<Voice>>(emptyList())
    val availableVoices = _availableVoices.asStateFlow()

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
                updateVoices()
            }
        }
    }

    private fun updateVoices() {
        val allowedLanguages = setOf("it", "fr", "en", "de", "es")
        _availableVoices.value = tts?.voices?.toList()
            ?.filter { voice ->
                val lang = voice.locale.language
                allowedLanguages.contains(lang)
            }
            ?.sortedWith(
                compareBy<Voice> { it.locale.displayCountry }
                    .thenBy { it.locale.displayLanguage }
                    .thenBy { it.name }
            ) ?: emptyList()
    }

    fun speak(text: String, language: Language, pitch: Float = 1.0f, rate: Float = 1.0f, voiceName: String? = null) {
        if (!isInitialized) return

        val locale = when (language) {
            Language.ITALIAN -> Locale.ITALIAN
            Language.FRENCH -> Locale.FRENCH
            Language.ENGLISH -> Locale.ENGLISH
            Language.GERMAN -> Locale.GERMAN
            Language.SPANISH -> Locale("es")
        }

        tts?.apply {
            setLanguage(locale)
            setPitch(pitch)
            setSpeechRate(rate)
            
            voiceName?.let { name ->
                voices?.find { it.name == name }?.let { voice ->
                    setVoice(voice)
                }
            }

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
