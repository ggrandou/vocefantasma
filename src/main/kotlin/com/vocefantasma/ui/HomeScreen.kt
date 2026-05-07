package com.vocefantasma.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vocefantasma.viewmodel.AppMode
import com.vocefantasma.viewmodel.AutoState
import com.vocefantasma.viewmodel.MainViewModel

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val mode by viewModel.mode.collectAsState()
    val autoState by viewModel.autoState.collectAsState()
    val currentCollection by viewModel.currentCollection.collectAsState()
    val availableCollections by viewModel.availableCollections.collectAsState()
    val currentDb by viewModel.currentDb.collectAsState()
    val isSilent by viewModel.isSilent.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top: Collection Selector
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Collection", style = MaterialTheme.typography.labelLarge)
            var expanded by remember { mutableStateOf(false) }
            Box {
                TextButton(onClick = { expanded = true }) {
                    Text(currentCollection.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    availableCollections.forEach { collection ->
                        DropdownMenuItem(
                            text = { Text(collection.name) },
                            onClick = {
                                viewModel.setCollection(collection)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        // Center: Spirit Button
        Box(contentAlignment = Alignment.Center) {
            val buttonColor by animateColorAsState(
                targetValue = when (autoState) {
                    AutoState.IDLE -> if (mode == AppMode.MANUAL) Color(0xFF6200EE) else Color.Gray
                    AutoState.LISTENING -> Color(0xFF03DAC6)
                    AutoState.WAITING_FOR_SILENCE -> Color(0xFFFFAB00)
                    AutoState.DECISION -> Color(0xFFBB86FC)
                    AutoState.DELAYED_ACTION -> Color(0xFF018786)
                    AutoState.SPEAKING -> Color(0xFFCF6679)
                },
                animationSpec = tween(500)
            )

            val infiniteTransition = rememberInfiniteTransition()
            val scale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = if (autoState != AutoState.IDLE) 1.1f else 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

            Surface(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .clickable { viewModel.onMainButtonClick() }
                    .padding((200 * (scale - 1)).dp / 2),
                color = buttonColor,
                shadowElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (mode == AppMode.MANUAL) "PARLER" else if (autoState == AutoState.IDLE) "START" else "STOP",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }
        }

        // Bottom: Mode & Visualizer
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("MANUAL")
                Switch(
                    checked = mode == AppMode.AUTO,
                    onCheckedChange = { viewModel.setMode(if (it) AppMode.AUTO else AppMode.MANUAL) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text("AUTO")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (mode == AppMode.AUTO) {
                Text("État: ${autoState.name}")
                Spacer(modifier = Modifier.height(8.dp))
                // Simple DB visualizer
                LinearProgressIndicator(
                    progress = (currentDb + 100) / 100,
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = if (isSilent) Color.Green else Color.Red
                )
            }
        }
    }
}
