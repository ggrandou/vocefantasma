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
        name = "IT pizza",
        language = Language.ITALIAN,
        isDefault = true,
        phrases = listOf(
            "Margherita", "Marinara", "Quattro Stagioni", "Carbonara", "Quattro Formaggi",
            "Napoletana", "Capricciosa", "Diavola", "Boscaiola", "Frutti di Mare",
            "Prosciutto e Funghi", "Ortolana", "Bufalina", "Tonno e Cipolla", "Calzone",
            "Focaccia al Rosmarino", "Salsiccia e Friarielli", "Pugliese", "Romana", "Siciliana",
            "Tirolese", "Valtellinese", "Americana", "Viennese", "Messicana",
            "Bismark", "Mimosa", "Parmigiana", "Zucchini e Gamberetti", "Tartufata",
            "Pesto e Pomodorini", "Bresaola e Rucola", "Stracchino e Crudo", "Patatosa", "Caprese",
            "Fiori di Zucca", "Cacio e Pepe", "Amatriciana", "Gricia", "Norcina",
            "Radicchio e Gorgonzola", "Speck e Mascarpone", "Porcini e Provola", "Melanzane", "Peperoni",
            "Friarielli e Burrata", "Acciughe e Capperi", "Mortadella e Pistacchio", "Nduja e Stracciatella", "Cotto e Mais",
            "Spinaci e Ricotta", "Uovo e Pancetta", "Salmone e Philadelphia", "Tartufo Bianco", "Porchetta"
        )
    )

    val frPizza = PhraseCollection(
        name = "FR pizza",
        language = Language.FRENCH,
        isDefault = true,
        phrases = listOf(
            "Reine", "Marguerita", "Quatre Fromages", "Royale", "Calzone",
            "Napolitaine", "Hawaïenne", "Orientale", "Campione", "Végétarienne",
            "Bolognaise", "Pêcheur", "Nordique", "Chèvre Miel", "Savoyarde",
            "Tartiflette", "Auvergnate", "Paysanne", "Indienne", "Kebab",
            "Mexicaine", "Texane", "Burger", "Cannibale", "Fermière",
            "Sicilienne", "Romaine", "Provençale", "Niçoise", "Alsacienne",
            "Carbonara", "Berrichonne", "Corse", "Basque", "Bretonne",
            "Océane", "Saumon", "Thon", "Fruits de Mer", "Saint-Jacques",
            "Magret", "Périgourdine", "Landaise", "Gersoise", "Aveyronnaise",
            "Seguin", "Chorizo", "Andalouse", "Barcelonaise", "Madrid", "Valence",
            "Lisbonne", "Porto", "Monaco"
        )
    )

    val allDefaults = listOf(itPizza, frPizza)
}
