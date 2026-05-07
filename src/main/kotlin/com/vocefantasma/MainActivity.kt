package com.vocefantasma

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vocefantasma.audio.SilenceDetector
import com.vocefantasma.audio.TtsManager
import com.vocefantasma.ui.VoceFantasmaApp
import com.vocefantasma.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    
    private lateinit var ttsManager: TtsManager
    private val silenceDetector = SilenceDetector()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Permission handled
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        
        ttsManager = TtsManager(this)
        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)

        setContent {
            // In a real app, we'd use Hilt or a Factory
            val viewModel: MainViewModel = viewModel(
                factory = MainViewModelFactory(ttsManager, silenceDetector)
            )
            
            MaterialTheme(colorScheme = darkColorScheme()) {
                VoceFantasmaApp(viewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager.release()
        silenceDetector.stop()
    }
}

// Minimal Factory for ViewModel without DI framework
class MainViewModelFactory(
    private val ttsManager: TtsManager,
    private val silenceDetector: SilenceDetector
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(ttsManager, silenceDetector) as T
    }
}
