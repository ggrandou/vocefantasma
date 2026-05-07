package com.vocefantasma.audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.log10
import kotlin.math.sqrt

class SilenceDetector {
    private val sampleRate = 44100
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

    private var recorder: AudioRecord? = null
    private var job: Job? = null

    private val _currentDb = MutableStateFlow(-100f)
    val currentDb = _currentDb.asStateFlow()

    private val _isSilent = MutableStateFlow(true)
    val isSilent = _isSilent.asStateFlow()

    @SuppressLint("MissingPermission")
    fun start(scope: CoroutineScope, thresholdDb: Float, silenceDurationMs: Long) {
        if (recorder != null) return

        recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize
        )

        val buffer = ShortArray(bufferSize)
        recorder?.startRecording()

        job = scope.launch(Dispatchers.IO) {
            var lastSoundTime = System.currentTimeMillis()

            try {
                while (recorder?.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                    val readSize = recorder?.read(buffer, 0, buffer.size) ?: 0
                    if (readSize > 0) {
                        val db = calculateDecibels(buffer, readSize)
                        _currentDb.value = db

                        val now = System.currentTimeMillis()
                        if (db >= thresholdDb) {
                            lastSoundTime = now
                            _isSilent.value = false
                        } else {
                            if (now - lastSoundTime >= silenceDurationMs) {
                                _isSilent.value = true
                            }
                        }
                    }
                    delay(50) // Reduce CPU usage
                }
            } finally {
                stop()
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
        recorder?.apply {
            if (recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                stop()
            }
            release()
        }
        recorder = null
        _currentDb.value = -100f
        _isSilent.value = true
    }

    private fun calculateDecibels(buffer: ShortArray, readSize: Int): Float {
        var sum = 0.0
        for (i in 0 until readSize) {
            sum += buffer[i].toDouble() * buffer[i].toDouble()
        }
        val rms = sqrt(sum / readSize)
        return if (rms > 0) (20 * log10(rms / 32767.0)).toFloat() else -100f
    }
}
