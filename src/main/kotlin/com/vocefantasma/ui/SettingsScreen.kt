package com.vocefantasma.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vocefantasma.viewmodel.MainViewModel
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    val settings by viewModel.settings.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Réglages Audio", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Seuil de silence: ${settings.silenceThresholdDb.roundToInt()} dB")
        Slider(
            value = settings.silenceThresholdDb,
            onValueChange = { viewModel.updateSettings(settings.copy(silenceThresholdDb = it)) },
            valueRange = -100f..-20f
        )

        Text("Durée du silence: ${settings.silenceDurationMs} ms")
        Slider(
            value = settings.silenceDurationMs.toFloat(),
            onValueChange = { viewModel.updateSettings(settings.copy(silenceDurationMs = it.toLong())) },
            valueRange = 500f..5000f,
            steps = 9
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        Text("Logique Aléatoire", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Probabilité de réponse: ${(settings.responseProbability * 100).roundToInt()}%")
        Slider(
            value = settings.responseProbability,
            onValueChange = { viewModel.updateSettings(settings.copy(responseProbability = it)) },
            valueRange = 0f..1f
        )

        Text("Attente Min: ${settings.minWaitSeconds}s")
        Slider(
            value = settings.minWaitSeconds.toFloat(),
            onValueChange = { viewModel.updateSettings(settings.copy(minWaitSeconds = it.roundToInt())) },
            valueRange = 1f..30f
        )

        Text("Attente Max: ${settings.maxWaitSeconds}s")
        Slider(
            value = settings.maxWaitSeconds.toFloat(),
            onValueChange = { 
                if (it >= settings.minWaitSeconds) {
                    viewModel.updateSettings(settings.copy(maxWaitSeconds = it.roundToInt())) 
                }
            },
            valueRange = 1f..30f
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        Text("TTS", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Pitch: ${settings.ttsPitch}")
        Slider(
            value = settings.ttsPitch,
            onValueChange = { viewModel.updateSettings(settings.copy(ttsPitch = it)) },
            valueRange = 0.5f..2.0f
        )

        Text("Vitesse: ${settings.ttsRate}")
        Slider(
            value = settings.ttsRate,
            onValueChange = { viewModel.updateSettings(settings.copy(ttsRate = it)) },
            valueRange = 0.5f..2.0f
        )
    }
}
