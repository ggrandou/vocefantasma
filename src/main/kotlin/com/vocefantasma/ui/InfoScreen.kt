package com.vocefantasma.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vocefantasma.R

@Composable
fun InfoScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Voce Fantasma Di Milano",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Une parodie des applications de Spirit Voice.\n\n" +
            "Cette application est purement satirique. Elle ne détecte pas les fantômes, " +
            "mais elle connaît très bien les pizzas italiennes.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            "Instructions:",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            "1. Choisissez votre collection.\n" +
            "2. Mode MANUEL: Appuyez pour parler.\n" +
            "3. Mode AUTO: L'app écoute. Quand vous vous taisez, elle peut répondre " +
            "après un délai aléatoire. Si vous parlez pendant l'attente, elle réinitialise son cycle.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))
        CreditRow(
            imageRes = R.drawable.mikedyson21,
            text = "sur une idée de MikeDyson21 dans CQFD"
        )
        Spacer(modifier = Modifier.height(16.dp))
        CreditRow(
            imageRes = R.drawable.rockfort,
            text = "réalisation Rockfort"
        )
    }
}

@Composable
private fun CreditRow(imageRes: Int, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}
