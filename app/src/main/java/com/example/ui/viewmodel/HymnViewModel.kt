package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.Hymn
import com.example.data.repository.HymnRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class BottomTab {
    HOME, SEARCH, FAVORITES, SETTINGS
}

sealed class AppScreen {
    object Splash : AppScreen()
    data class Main(val tab: BottomTab = BottomTab.HOME) : AppScreen()
    data class Detail(val hymn: Hymn) : AppScreen()
    data class CategoryView(val category: String) : AppScreen()
    object DonatePage : AppScreen()
    object ContactPage : AppScreen()
    object AboutPage : AppScreen()
    data class ChurchInfo(val title: String, val contentKey: String) : AppScreen()
}

class HymnViewModel(private val repository: HymnRepository) : ViewModel() {

    val hymns = repository.hymns
    val isLoading = repository.isLoading
    val loadError = repository.loadError
    val isUploading400 = repository.isUploading400
    val uploadStatusMessage = repository.uploadStatusMessage
    val favoriteIds = repository.favoriteIds
    val recentIds = repository.recentIds
    val fontSize = repository.fontSize

    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Splash)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _selectedHymn = MutableStateFlow<Hymn?>(null)
    val selectedHymn: StateFlow<Hymn?> = _selectedHymn.asStateFlow()

    // Filtered Hymns according to search query and category
    val filteredHymns: StateFlow<List<Hymn>> = combine(
        hymns,
        _searchQuery,
        _selectedCategory
    ) { all, query, category ->
        var list = all

        if (!category.isNullOrBlank()) {
            list = list.filter { it.category.equals(category, ignoreCase = true) }
        }

        val q = query.trim()
        if (q.isNotEmpty()) {
            val numQuery = q.toIntOrNull()
            if (numQuery != null) {
                // Exact or prefix match on hymn number
                list = list.filter {
                    it.number == numQuery || it.number.toString().startsWith(q) ||
                            it.title.contains(q, ignoreCase = true)
                }.sortedBy { if (it.number == numQuery) 0 else 1 }
            } else {
                list = list.filter {
                    it.title.contains(q, ignoreCase = true) ||
                            it.lyrics.contains(q, ignoreCase = true) ||
                            it.chorus.contains(q, ignoreCase = true) ||
                            it.category.contains(q, ignoreCase = true)
                }
            }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Favorite Hymns
    val favoriteHymns: StateFlow<List<Hymn>> = combine(hymns, favoriteIds) { all, favs ->
        all.filter { favs.contains(it.number) }.sortedBy { it.number }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Recent Hymns
    val recentHymns: StateFlow<List<Hymn>> = combine(hymns, recentIds) { all, recents ->
        recents.mapNotNull { num -> all.find { it.number == num } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Featured Hymns for Home Screen
    val featuredHymns: StateFlow<List<Hymn>> = hymns.combine(favoriteIds) { all, _ ->
        if (all.isEmpty()) emptyList()
        else {
            val picks = listOf(1, 2, 3, 4, 5, 7, 8, 10)
            val featured = all.filter { picks.contains(it.number) }
            if (featured.isNotEmpty()) featured else all.take(6)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        refreshHymns()
    }

    fun refreshHymns() {
        viewModelScope.launch {
            repository.fetchHymns()
        }
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun navigateBack() {
        when (_currentScreen.value) {
            is AppScreen.Detail, is AppScreen.CategoryView, is AppScreen.ChurchInfo,
            AppScreen.DonatePage, AppScreen.ContactPage, AppScreen.AboutPage -> {
                _currentScreen.value = AppScreen.Main(BottomTab.HOME)
            }
            is AppScreen.Main -> {
                // Keep on main
            }
            AppScreen.Splash -> {
                _currentScreen.value = AppScreen.Main(BottomTab.HOME)
            }
        }
    }

    fun seedAdditional400() {
        viewModelScope.launch {
            repository.seedAdditional400ToFirestore()
        }
    }

    fun openHymn(hymn: Hymn) {
        _selectedHymn.value = hymn
        repository.markRecent(hymn.number)
        _currentScreen.value = AppScreen.Detail(hymn)
    }

    fun nextHymn() {
        val current = _selectedHymn.value ?: return
        val all = hymns.value
        val currentIndex = all.indexOfFirst { it.number == current.number }
        if (currentIndex != -1 && currentIndex < all.size - 1) {
            openHymn(all[currentIndex + 1])
        }
    }

    fun prevHymn() {
        val current = _selectedHymn.value ?: return
        val all = hymns.value
        val currentIndex = all.indexOfFirst { it.number == current.number }
        if (currentIndex > 0) {
            openHymn(all[currentIndex - 1])
        }
    }

    fun toggleFavorite(hymn: Hymn) {
        repository.toggleFavorite(hymn.number)
    }

    fun isFavorite(hymnNumber: Int): Boolean {
        return repository.isFavorite(hymnNumber)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun increaseFontSize() {
        repository.setFontSize(fontSize.value + 2f)
    }

    fun decreaseFontSize() {
        repository.setFontSize(fontSize.value - 2f)
    }

    fun resetFontSize() {
        repository.setFontSize(18f)
    }
}

class HymnViewModelFactory(private val repository: HymnRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HymnViewModel::class.java)) {
            return HymnViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
