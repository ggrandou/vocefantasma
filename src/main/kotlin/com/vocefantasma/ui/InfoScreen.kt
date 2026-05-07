package com.vocefantasma.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
        Spacer(modifier = Modifier.height(40.dp))
        PrivacyPolicySection()
    }
}

@Composable
private fun PrivacyPolicySection() {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Politique de confidentialité",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Dernière mise à jour : mai 2025",
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            PrivacyParagraph(
                title = "Microphone",
                body = "L'application utilise le microphone uniquement en Mode AUTO pour détecter " +
                       "les silences et les sons ambiants. L'audio n'est jamais enregistré, " +
                       "stocké ni transmis. Le traitement s'effectue entièrement sur l'appareil, " +
                       "en temps réel."
            )
            PrivacyParagraph(
                title = "Données personnelles",
                body = "Aucune donnée personnelle n'est collectée. L'application ne crée pas " +
                       "de compte utilisateur, n'accède pas aux contacts, à la localisation, " +
                       "ni à aucun autre capteur ou fichier."
            )
            PrivacyParagraph(
                title = "Réseau",
                body = "L'application ne dispose d'aucune permission réseau et ne communique " +
                       "avec aucun serveur externe."
            )
            PrivacyParagraph(
                title = "Analytique et publicité",
                body = "L'application ne contient aucun SDK d'analyse, de publicité ou de suivi."
            )
            PrivacyParagraph(
                title = "Contact",
                body = "Pour toute question concernant cette politique, contactez le développeur " +
                       "via la fiche de l'application sur le Google Play Store."
            )
        }
    }
}

@Composable
private fun PrivacyParagraph(title: String, body: String) {
    Text(title, style = MaterialTheme.typography.labelMedium)
    Spacer(modifier = Modifier.height(4.dp))
    Text(body, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Start)
    Spacer(modifier = Modifier.height(12.dp))
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
