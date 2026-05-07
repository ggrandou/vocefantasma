package com.vocefantasma.models

import java.util.UUID

enum class Language(val code: String) {
    ITALIAN("it"),
    FRENCH("fr")
}

data class PhraseCollection(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val language: Language,
    val isDefault: Boolean = false,
    val phrases: List<String>
)

data class AppSettings(
    val silenceThresholdDb: Float = -50f,
    val silenceDurationMs: Long = 1500L,
    val responseProbability: Float = 0.5f, // 0.0 to 1.0
    val minWaitSeconds: Int = 1,
    val maxWaitSeconds: Int = 10,
    val ttsPitch: Float = 1.0f,
    val ttsRate: Float = 1.0f
)

object DefaultData {
    val itPizza = PhraseCollection(
        name = "🇮🇹 pizza",
        language = Language.ITALIAN,
        isDefault = true,
        phrases = listOf(
            "Pizza Margherita", "Pizza Marinara", "Pizza Quattro Stagioni", "Pizza Carbonara", "Pizza Quattro Formaggi",
            "Pizza Napoletana", "Pizza Capricciosa", "Pizza Diavola", "Pizza Boscaiola", "Pizza Frutti di Mare",
            "Pizza Prosciutto e Funghi", "Pizza Ortolana", "Pizza Bufalina", "Pizza Tonno e Cipolla", "Pizza Calzone",
            "Pizza Focaccia al Rosmarino", "Pizza Salsiccia e Friarielli", "Pizza Pugliese", "Pizza Romana", "Pizza Siciliana",
            "Pizza Tirolese", "Pizza Valtellinese", "Pizza Americana", "Pizza Viennese", "Pizza Messicana",
            "Pizza Bismark", "Pizza Mimosa", "Pizza Parmigiana", "Pizza Zucchini e Gamberetti", "Pizza Tartufata",
            "Pizza Pesto e Pomodorini", "Pizza Bresaola e Rucola", "Pizza Stracchino e Crudo", "Pizza Patatosa", "Pizza Caprese",
            "Pizza Fiori di Zucca", "Pizza Cacio e Pepe", "Pizza Amatriciana", "Pizza Gricia", "Pizza Norcina",
            "Pizza Radicchio e Gorgonzola", "Pizza Speck e Mascarpone", "Pizza Porcini e Provola", "Pizza Melanzane", "Pizza Peperoni",
            "Pizza Friarielli e Burrata", "Pizza Acciughe e Capperi", "Pizza Mortadella e Pistacchio", "Pizza Nduja e Stracciatella", "Pizza Cotto e Mais",
            "Pizza Spinaci e Ricotta", "Pizza Uovo e Pancetta", "Pizza Salmone e Philadelphia", "Pizza Tartufo Bianco", "Pizza Porchetta"
        )
    )

    val frPizza = PhraseCollection(
        name = "🇫🇷 pizza",
        language = Language.FRENCH,
        isDefault = true,
        phrases = listOf(
            "Pizza Reine", "Pizza Marguerita", "Pizza Quatre Fromages", "Pizza Royale", "Pizza Calzone",
            "Pizza Napolitaine", "Pizza Hawaïenne", "Pizza Orientale", "Pizza Campione", "Pizza Végétarienne",
            "Pizza Bolognaise", "Pizza Pêcheur", "Pizza Nordique", "Pizza Chèvre Miel", "Pizza Savoyarde",
            "Pizza Tartiflette", "Pizza Auvergnate", "Pizza Paysanne", "Pizza Indienne", "Pizza Kebab",
            "Pizza Mexicaine", "Pizza Texane", "Pizza Burger", "Pizza Cannibale", "Pizza Fermière",
            "Pizza Sicilienne", "Pizza Romaine", "Pizza Provençale", "Pizza Niçoise", "Pizza Alsacienne",
            "Pizza Carbonara", "Pizza Berrichonne", "Pizza Corse", "Pizza Basque", "Pizza Bretonne",
            "Pizza Océane", "Pizza Saumon", "Pizza Thon", "Pizza Fruits de Mer", "Pizza Saint-Jacques",
            "Pizza Magret", "Pizza Périgourdine", "Pizza Landaise", "Pizza Gersoise", "Pizza Aveyronnaise",
            "Pizza Seguin", "Pizza Chorizo", "Pizza Andalouse", "Pizza Barcelonaise", "Pizza Madrid", "Pizza Valence",
            "Pizza Lisbonne", "Pizza Porto", "Pizza Monaco"
        )
    )

    val allDefaults = listOf(itPizza, frPizza)
}
