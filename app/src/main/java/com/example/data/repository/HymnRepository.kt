package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.R
import com.example.data.datasource.HymnCatalog
import com.example.data.model.Hymn
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.WriteBatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HymnRepository(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("triune_hymn_prefs", Context.MODE_PRIVATE)

    // Complete library initialized with 1 to 1000 hymns
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

    private fun getSafeFirestore(): FirebaseFirestore? {
        return try {
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseApp.initializeApp(context)
            }
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            Log.w("HymnRepository", "Could not get FirebaseFirestore: ${e.message}")
            null
        }
    }

    suspend fun fetchHymns(): Result<List<Hymn>> = withContext(Dispatchers.IO) {
        _isLoading.value = true
        _loadError.value = null

        try {
            val firestore = getSafeFirestore()
            if (firestore == null) {
                _isLoading.value = false
                return@withContext Result.success(_hymns.value)
            }

            var snapshot: QuerySnapshot? = null

            // Query with .orderBy("number").limit(1000) as requested
            try {
                snapshot = firestore.collection("hymns")
                    .orderBy("number")
                    .limit(1000)
                    .get()
                    .await()
            } catch (e: Exception) {
                Log.w("HymnRepository", "Ordered query failed (indexing may be in progress), fallback to unordered", e)
                try {
                    snapshot = firestore.collection("hymns").limit(1000).get().await()
                } catch (e2: Exception) {
                    Log.w("HymnRepository", "Direct collection fetch failed: ${e2.message}")
                }
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val remoteHymns = snapshot.documents.mapNotNull { parseHymn(it) }
                if (remoteHymns.isNotEmpty()) {
                    // Merge remote hymns over base catalog (remote takes precedence for matching numbers)
                    val hymnMap = fullLibrary.associateBy { it.number }.toMutableMap()
                    remoteHymns.forEach { remote ->
                        hymnMap[remote.number] = remote
                    }
                    val combined = hymnMap.values.sortedBy { it.number }
                    _hymns.value = combined
                    _isLoading.value = false
                    Log.d("HymnRepository", "Merged ${remoteHymns.size} remote hymns. Total: ${combined.size}")
                    return@withContext Result.success(combined)
                }
            }

            _isLoading.value = false
            Result.success(_hymns.value)
        } catch (e: Exception) {
            Log.e("HymnRepository", "Error loading from Firestore", e)
            _loadError.value = e.localizedMessage
            _isLoading.value = false
            Result.failure(e)
        }
    }

    /**
     * Logic to add the 400 additional hymns (601-750 Christmas, 751-850 Easter, 851-1000 Others)
     * into Firestore collection "hymns" while keeping existing 600 safe.
     */
    suspend fun seedAdditional400ToFirestore(): Result<Int> = withContext(Dispatchers.IO) {
        _isUploading400.value = true
        _uploadStatusMessage.value = "Connecting to Firestore..."

        val firestore = getSafeFirestore()
        if (firestore == null) {
            _isUploading400.value = false
            _uploadStatusMessage.value = "Firebase Firestore unavailable."
            return@withContext Result.failure(IllegalStateException("Firebase not initialized"))
        }

        try {
            val hymnsToUpload = fullLibrary.filter { it.number in 601..1000 }
            val total = hymnsToUpload.size
            var addedCount = 0

            // Upload in batches of 50 to respect Firestore limits
            val chunks = hymnsToUpload.chunked(50)
            for ((index, chunk) in chunks.withIndex()) {
                val batch = firestore.batch()
                for (hymn in chunk) {
                    val docRef = firestore.collection("hymns").document(hymn.number.toString())
                    val data = hashMapOf(
                        "number" to hymn.number,
                        "title" to hymn.title,
                        "lyrics" to hymn.lyrics,
                        "chorus" to hymn.chorus,
                        "category" to hymn.category,
                        "author" to hymn.author,
                        "keySignature" to hymn.keySignature,
                        "meter" to hymn.meter
                    )
                    batch.set(docRef, data)
                }
                batch.commit().await()
                addedCount += chunk.size
                _uploadStatusMessage.value = "Synced $addedCount of $total hymns to Firestore..."
            }

            _uploadStatusMessage.value = "Successfully added all 400 hymns (601-1000) to Firestore!"
            _isUploading400.value = false
            // Refresh local list
            fetchHymns()
            Result.success(addedCount)
        } catch (e: Exception) {
            Log.e("HymnRepository", "Failed to seed 400 hymns to Firestore", e)
            _uploadStatusMessage.value = "Upload error: ${e.localizedMessage}"
            _isUploading400.value = false
            Result.failure(e)
        }
    }

    private fun parseHymn(doc: DocumentSnapshot): Hymn? {
        val number = doc.getLong("number")?.toInt()
            ?: (doc.get("number") as? String)?.toIntOrNull()
            ?: doc.id.filter { it.isDigit() }.toIntOrNull()
            ?: 0

        if (number <= 0) return null

        val title = doc.getString("title")
            ?: doc.getString("name")
            ?: "Hymn $number"

        val lyricsRaw = doc.getString("lyrics")
            ?: (doc.get("verses") as? List<*>)?.joinToString("\n\n")
            ?: doc.getString("content")
            ?: doc.getString("text")
            ?: ""

        val chorus = doc.getString("chorus")
            ?: doc.getString("refrain")
            ?: ""

        val category = doc.getString("category")
            ?: doc.getString("topic")
            ?: when {
                number in 601..750 -> "Christmas"
                number in 751..850 -> "Easter"
                number in 851..1000 -> "Others"
                else -> "General Hymn"
            }

        val author = doc.getString("author")
            ?: doc.getString("writer")
            ?: ""

        val keySignature = doc.getString("keySignature")
            ?: doc.getString("key")
            ?: ""

        val meter = doc.getString("meter")
            ?: ""

        return Hymn(
            id = doc.id,
            number = number,
            title = title,
            lyrics = lyricsRaw,
            chorus = chorus,
            category = category,
            author = author,
            keySignature = keySignature,
            meter = meter
        )
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
