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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
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
        // Top: Titre
        Text(
            "Voce Fantasma Di Milano",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

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
            val glowFraction by infiniteTransition.animateFloat(
                initialValue = 0.4f,
                targetValue = if (autoState != AutoState.IDLE) 1.0f else 0.4f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1200, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(300.dp)
                    .drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    buttonColor.copy(alpha = 0.55f),
                                    Color.Transparent
                                ),
                                center = center,
                                radius = size.minDimension / 2f * glowFraction
                            ),
                            radius = size.minDimension / 2f
                        )
                    }
            ) {
                Surface(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .clickable { viewModel.onMainButtonClick() },
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
        }

        // Bottom: Collection + Mode + Visualizer (hauteur fixe pour éviter les décalages)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Collection Selector
            Text("Collection", style = MaterialTheme.typography.labelLarge)
            var expanded by remember { mutableStateOf(false) }
            Box {
                TextButton(onClick = { expanded = true }) {
                    Text(currentCollection.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
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

            Spacer(modifier = Modifier.height(8.dp))

            // Mode toggle
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("MANUEL")
                Switch(
                    checked = mode == AppMode.AUTO,
                    onCheckedChange = { viewModel.setMode(if (it) AppMode.AUTO else AppMode.MANUAL) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text("AUTO")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Auto state info — toujours présent pour garder une hauteur fixe
            Text(
                text = if (mode == AppMode.AUTO) "État: ${autoState.name}" else "",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.height(20.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = if (mode == AppMode.AUTO) (currentDb + 100) / 100 else 0f,
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = if (isSilent) Color.Green else Color.Red,
                trackColor = if (mode == AppMode.AUTO) Color.DarkGray else Color.Transparent
            )
        }
    }
}
