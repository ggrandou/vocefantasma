package com.vocefantasma.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vocefantasma.audio.SilenceDetector
import com.vocefantasma.audio.TtsManager
import com.vocefantasma.models.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.random.Random

enum class AppMode { MANUAL, AUTO }

enum class AutoState { IDLE, LISTENING, WAITING_FOR_SILENCE, DECISION, DELAYED_ACTION, SPEAKING }

class MainViewModel(
    private val ttsManager: TtsManager,
    private val silenceDetector: SilenceDetector
) : ViewModel() {

    private val _settings = MutableStateFlow(AppSettings())
    val settings = _settings.asStateFlow()

    private val _currentCollection = MutableStateFlow(DefaultData.itPizza)
    val currentCollection = _currentCollection.asStateFlow()

    private val _availableCollections = MutableStateFlow(DefaultData.allDefaults)
    val availableCollections = _availableCollections.asStateFlow()

    private val _mode = MutableStateFlow(AppMode.MANUAL)
    val mode = _mode.asStateFlow()

    private val _autoState = MutableStateFlow(AutoState.IDLE)
    val autoState = _autoState.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening = _isListening.asStateFlow()

    private var autoJob: Job? = null

    val currentDb = silenceDetector.currentDb
    val isSilent = silenceDetector.isSilent
    val availableVoices = ttsManager.availableVoices

    fun setMode(mode: AppMode) {
        _mode.value = mode
        if (mode == AppMode.MANUAL) {
            stopAutoMode()
        }
    }

    fun setCollection(collection: PhraseCollection) {
        _currentCollection.value = collection
    }

    fun onMainButtonClick() {
        if (_mode.value == AppMode.MANUAL) {
            speakRandomPhrase()
        } else {
            toggleAutoMode()
        }
    }

    private fun speakRandomPhrase() {
        val collection = _currentCollection.value
        if (collection.phrases.isNotEmpty()) {
            val phrase = collection.phrases.random()
            ttsManager.speak(
                phrase,
                collection.language,
                _settings.value.ttsPitch,
                _settings.value.ttsRate,
                _settings.value.ttsVoiceName
            )
        }
    }

    private fun toggleAutoMode() {
        if (_isListening.value) {
            stopAutoMode()
        } else {
            startAutoMode()
        }
    }

    private fun startAutoMode() {
        _isListening.value = true
        _autoState.value = AutoState.LISTENING
        silenceDetector.start(viewModelScope, _settings.value.silenceThresholdDb, _settings.value.silenceDurationMs)
        
        autoJob = viewModelScope.launch {
            runStateMachine()
        }
    }

    private fun stopAutoMode() {
        _isListening.value = false
        _autoState.value = AutoState.IDLE
        silenceDetector.stop()
        autoJob?.cancel()
        autoJob = null
    }

    private suspend fun runStateMachine() {
        while (currentCoroutineContext().isActive) {
            when (_autoState.value) {
                AutoState.LISTENING -> {
                    // Wait for sound (not silent)
                    isSilent.first { !it }
                    _autoState.value = AutoState.WAITING_FOR_SILENCE
                }
                AutoState.WAITING_FOR_SILENCE -> {
                    // Wait for silence to be established
                    isSilent.first { it }
                    _autoState.value = AutoState.DECISION
                }
                AutoState.DECISION -> {
                    val roll = Random.nextFloat()
                    if (roll <= _settings.value.responseProbability) {
                        _autoState.value = AutoState.DELAYED_ACTION
                    } else {
                        // Reset to listening if decision is NO
                        _autoState.value = AutoState.LISTENING
                    }
                }
                AutoState.DELAYED_ACTION -> {
                    val waitTime = Random.nextInt(_settings.value.minWaitSeconds, _settings.value.maxWaitSeconds + 1) * 1000L
                    val startTime = System.currentTimeMillis()
                    
                    var interrupted = false
                    while (System.currentTimeMillis() - startTime < waitTime) {
                        if (!isSilent.value) {
                            interrupted = true
                            break
                        }
                        delay(100)
                    }

                    if (interrupted) {
                        _autoState.value = AutoState.LISTENING
                    } else {
                        _autoState.value = AutoState.SPEAKING
                    }
                }
                AutoState.SPEAKING -> {
                    speakRandomPhrase()
                    delay(1000) // Avoid immediate re-trigger
                    _autoState.value = AutoState.LISTENING
                }
                else -> delay(100)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopAutoMode()
        ttsManager.release()
    }
    
    // Settings setters
    fun updateSettings(newSettings: AppSettings) {
        _settings.value = newSettings
    }
}
