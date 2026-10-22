package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.datasource.HymnCatalog
import com.example.data.model.Hymn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class HymnRepository(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("triune_hymn_prefs", Context.MODE_PRIVATE)

    // Complete library offline - 1 to 1000 hymns from local catalog
    private val fullLibrary = HymnCatalog.getFullHymnLibrary()

    private val _hymns = MutableStateFlow<List<Hymn>>(fullLibrary)
    val hymns: StateFlow<List<Hymn>> = _hymns.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _loadError = MutableStateFlow<String?>(null)
    val loadError: StateFlow<String?> = _loadError.asStateFlow()

    private val _isUploading400 = MutableStateFlow(false)
    val isUploading400: StateFlow<Boolean> = _isUploading400.asStateFlow()

    private val _uploadStatusMessage = MutableStateFlow<String?>(null)
    val uploadStatusMessage: StateFlow<String?> = _uploadStatusMessage.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<Int>>(emptySet())
    val favoriteIds: StateFlow<Set<Int>> = _favoriteIds.asStateFlow()

    private val _recentIds = MutableStateFlow<List<Int>>(emptyList())
    val recentIds: StateFlow<List<Int>> = _recentIds.asStateFlow()

    private val _fontSize = MutableStateFlow(18f)
    val fontSize: StateFlow<Float> = _fontSize.asStateFlow()

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        val favs = prefs.getStringSet("favorite_hymn_numbers", emptySet()) ?: emptySet()
        _favoriteIds.value = favs.mapNotNull { it.toIntOrNull() }.toSet()

        val recentsStr = prefs.getString("recent_hymn_numbers", "") ?: ""
        if (recentsStr.isNotBlank()) {
            _recentIds.value = recentsStr.split(",")
                .mapNotNull { it.trim().toIntOrNull() }
                .distinct()
                .take(20)
        }

        _fontSize.value = prefs.getFloat("hymn_reading_font_size", 18f)
    }

    // OFFLINE VERSION - No Firebase
    suspend fun fetchHymns(): Result<List<Hymn>> = withContext(Dispatchers.IO) {
        _isLoading.value = true
        try {
            // Just return local catalog - fast and offline
            _hymns.value = fullLibrary
            _isLoading.value = false
            Result.success(fullLibrary)
        } catch (e: Exception) {
            _loadError.value = e.localizedMessage
            _isLoading.value = false
            Result.failure(e)
        }
    }

    // OFFLINE - does nothing, kept for UI compatibility
    suspend fun seedAdditional400ToFirestore(): Result<Int> = withContext(Dispatchers.IO) {
        _isUploading400.value = true
        _uploadStatusMessage.value = "Offline mode: All 1000 hymns already available locally!"
        kotlinx.coroutines.delay(1000)
        _isUploading400.value = false
        Result.success(0)
    }

    fun toggleFavorite(hymnNumber: Int) {
        val current = _favoriteIds.value.toMutableSet()
        if (current.contains(hymnNumber)) {
            current.remove(hymnNumber)
        } else {
            current.add(hymnNumber)
        }
        _favoriteIds.value = current
        prefs.edit().putStringSet("favorite_hymn_numbers", current.map { it.toString() }.toSet()).apply()
    }

    fun markRecent(hymnNumber: Int) {
        val current = _recentIds.value.filter { it != hymnNumber }.toMutableList()
        current.add(0, hymnNumber)
        val trimmed = current.take(25)
        _recentIds.value = trimmed
        prefs.edit().putString("recent_hymn_numbers", trimmed.joinToString(",")).apply()
    }

    fun setFontSize(size: Float) {
        val clamped = size.coerceIn(14f, 32f)
        _fontSize.value = clamped
        prefs.edit().putFloat("hymn_reading_font_size", clamped).apply()
    }

    fun isFavorite(hymnNumber: Int): Boolean {
        return _favoriteIds.value.contains(hymnNumber)
    }
}
