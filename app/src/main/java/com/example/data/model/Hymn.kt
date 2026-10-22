package com.example.data.model

data class Hymn(
    val id: String = "",
    val number: Int = 0,
    val title: String = "",
    val lyrics: String = "",
    val chorus: String = "",
    val category: String = "General Hymn",
    val keySignature: String = "",
    val author: String = "",
    val meter: String = ""
) {
    /**
     * Formats lyrics with verses and chorus cleanly.
     */
    fun formattedLyrics(): String {
        return lyrics.trim()
    }
}
