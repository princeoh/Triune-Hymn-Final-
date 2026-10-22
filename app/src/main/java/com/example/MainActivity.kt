package com.example

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.repository.HymnRepository
import com.example.ui.screens.AboutPage
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.ChurchInfoBottomSheet
import com.example.ui.screens.ContactPage
import com.example.ui.screens.DetailScreen
import com.example.ui.screens.DonatePage
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.openUrl
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldDark
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.OffWhiteBackground
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.RoyalBlueSurface
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.BottomTab
import com.example.ui.viewmodel.HymnViewModel
import com.example.ui.viewmodel.HymnViewModelFactory
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {

    private val repository by lazy { HymnRepository(applicationContext) }
    private val viewModel: HymnViewModel by viewModels { HymnViewModelFactory(repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_TriuneHymn)
        super.onCreate(savedInstanceState)
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
            }
        } catch (e: Exception) {
            Log.w("MainActivity", "FirebaseApp init warning: ${e.message}")
        }
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: HymnViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val hymns by viewModel.hymns.collectAsStateWithLifecycle()
    val filteredHymns by viewModel.filteredHymns.collectAsStateWithLifecycle()
    val favoriteHymns by viewModel.favoriteHymns.collectAsStateWithLifecycle()
    val recentHymns by viewModel.recentHymns.collectAsStateWithLifecycle()
    val featuredHymns by viewModel.featuredHymns.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isUploading400 by viewModel.isUploading400.collectAsStateWithLifecycle()
    val uploadStatusMessage by viewModel.uploadStatusMessage.collectAsStateWithLifecycle()
    val fontSize by viewModel.fontSize.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf(BottomTab.HOME) }
    var activeChurchSheet by remember { mutableStateOf<String?>(null) }
    var showCategories by remember { mutableStateOf(false) }

    Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
        when (screen) {
            is AppScreen.Splash -> {
                SplashScreen(
                    onTimeout = {
                        viewModel.navigateTo(AppScreen.Main(BottomTab.HOME))
                    }
                )
            }

            is AppScreen.Detail -> {
                DetailScreen(
                    hymn = screen.hymn,
                    isFavorite = viewModel.isFavorite(screen.hymn.number),
                    fontSizeSp = fontSize,
                    onBack = { viewModel.navigateBack() },
                    onToggleFavorite = { viewModel.toggleFavorite(screen.hymn) },
                    onNextHymn = { viewModel.nextHymn() },
                    onPrevHymn = { viewModel.prevHymn() },
                    onIncreaseFontSize = { viewModel.increaseFontSize() },
                    onDecreaseFontSize = { viewModel.decreaseFontSize() },
                    onResetFontSize = { viewModel.resetFontSize() }
                )
            }

            is AppScreen.CategoryView -> {
                CategoriesScreen(
                    hymns = hymns,
                    onCategoryClick = { category ->
                        viewModel.selectCategory(category)
                        viewModel.navigateTo(AppScreen.Main(BottomTab.SEARCH))
                    },
                    onBack = { viewModel.navigateBack() }
                )
            }

            is AppScreen.DonatePage -> {
                DonatePage(onBack = { viewModel.navigateBack() })
            }

            is AppScreen.ContactPage -> {
                ContactPage(onBack = { viewModel.navigateBack() })
            }

            is AppScreen.AboutPage -> {
                AboutPage(onBack = { viewModel.navigateBack() })
            }

            is AppScreen.ChurchInfo -> {
                ChurchInfoBottomSheet(
                    type = screen.contentKey,
                    onDismiss = { viewModel.navigateBack() }
                )
            }

            is AppScreen.Main -> {
                if (showCategories) {
                    CategoriesScreen(
                        hymns = hymns,
                        onCategoryClick = { category ->
                            viewModel.selectCategory(category)
                            activeTab = BottomTab.SEARCH
                            showCategories = false
                        },
                        onBack = { showCategories = false }
                    )
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = OffWhiteBackground,
                        bottomBar = {
                            NavigationBar(
                                containerColor = Color.White,
                                tonalElevation = 6.dp,
                                modifier = Modifier.testTag("bottom_nav_bar")
                            ) {
                                NavigationBarItem(
                                    selected = activeTab == BottomTab.HOME,
                                    onClick = { activeTab = BottomTab.HOME },
                                    icon = {
                                        Icon(
                                            imageVector = if (activeTab == BottomTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                                            contentDescription = "Home",
                                            modifier = Modifier.size(24.dp)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = "Home",
                                            fontSize = 12.sp,
                                            fontWeight = if (activeTab == BottomTab.HOME) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = RoyalBlue,
                                        selectedTextColor = RoyalBlue,
                                        indicatorColor = RoyalBlueSurface,
                                        unselectedIconColor = TextMuted,
                                        unselectedTextColor = TextMuted
                                    ),
                                    modifier = Modifier.testTag("nav_tab_home")
                                )

                                NavigationBarItem(
                                    selected = activeTab == BottomTab.SEARCH,
                                    onClick = { activeTab = BottomTab.SEARCH },
                                    icon = {
                                        Icon(
                                            imageVector = if (activeTab == BottomTab.SEARCH) Icons.Filled.Search else Icons.Outlined.Search,
                                            contentDescription = "Hymns",
                                            modifier = Modifier.size(24.dp)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = "Hymns",
                                            fontSize = 12.sp,
                                            fontWeight = if (activeTab == BottomTab.SEARCH) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = RoyalBlue,
                                        selectedTextColor = RoyalBlue,
                                        indicatorColor = RoyalBlueSurface,
                                        unselectedIconColor = TextMuted,
                                        unselectedTextColor = TextMuted
                                    ),
                                    modifier = Modifier.testTag("nav_tab_search")
                                )

                                NavigationBarItem(
                                    selected = activeTab == BottomTab.FAVORITES,
                                    onClick = { activeTab = BottomTab.FAVORITES },
                                    icon = {
                                        Icon(
                                            imageVector = if (activeTab == BottomTab.FAVORITES) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                            contentDescription = "Favorites",
                                            modifier = Modifier.size(24.dp)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = "Favorites",
                                            fontSize = 12.sp,
                                            fontWeight = if (activeTab == BottomTab.FAVORITES) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = GoldDark,
                                        selectedTextColor = GoldDark,
                                        indicatorColor = RoyalBlueSurface,
                                        unselectedIconColor = TextMuted,
                                        unselectedTextColor = TextMuted
                                    ),
                                    modifier = Modifier.testTag("nav_tab_favorites")
                                )

                                NavigationBarItem(
                                    selected = activeTab == BottomTab.SETTINGS,
                                    onClick = { activeTab = BottomTab.SETTINGS },
                                    icon = {
                                        Icon(
                                            imageVector = if (activeTab == BottomTab.SETTINGS) Icons.Filled.Settings else Icons.Outlined.Settings,
                                            contentDescription = "Settings",
                                            modifier = Modifier.size(24.dp)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = "Settings",
                                            fontSize = 12.sp,
                                            fontWeight = if (activeTab == BottomTab.SETTINGS) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = RoyalBlue,
                                        selectedTextColor = RoyalBlue,
                                        indicatorColor = RoyalBlueSurface,
                                        unselectedIconColor = TextMuted,
                                        unselectedTextColor = TextMuted
                                    ),
                                    modifier = Modifier.testTag("nav_tab_settings")
                                )
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (activeTab) {
                                BottomTab.HOME -> {
                                    HomeScreen(
                                        searchQuery = searchQuery,
                                        onSearchChange = { query ->
                                            viewModel.setSearchQuery(query)
                                            if (query.isNotEmpty()) {
                                                activeTab = BottomTab.SEARCH
                                            }
                                        },
                                        onSearchSubmit = { activeTab = BottomTab.SEARCH },
                                        recentHymns = recentHymns,
                                        featuredHymns = featuredHymns,
                                        isFavorite = { num -> viewModel.isFavorite(num) },
                                        onToggleFavorite = { hymn -> viewModel.toggleFavorite(hymn) },
                                        onHymnClick = { hymn -> viewModel.openHymn(hymn) },
                                        onQuickAccessClick = { actionId ->
                                            when (actionId) {
                                                "browse" -> {
                                                    viewModel.setSearchQuery("")
                                                    viewModel.selectCategory(null)
                                                    activeTab = BottomTab.SEARCH
                                                }
                                                "categories" -> {
                                                    showCategories = true
                                                }
                                                "donate" -> viewModel.navigateTo(AppScreen.DonatePage)
                                                "contact" -> viewModel.navigateTo(AppScreen.ContactPage)
                                                "about" -> viewModel.navigateTo(AppScreen.AboutPage)
                                                "website" -> activeChurchSheet = "website"
                                                "blog" -> activeChurchSheet = "blog"
                                            }
                                        },
                                        totalHymnsCount = hymns.size
                                    )
                                }

                                BottomTab.SEARCH -> {
                                    SearchScreen(
                                        searchQuery = searchQuery,
                                        onSearchChange = { query -> viewModel.setSearchQuery(query) },
                                        selectedCategory = selectedCategory,
                                        onCategorySelect = { cat -> viewModel.selectCategory(cat) },
                                        filteredHymns = filteredHymns,
                                        isLoading = isLoading,
                                        isFavorite = { num -> viewModel.isFavorite(num) },
                                        onToggleFavorite = { hymn -> viewModel.toggleFavorite(hymn) },
                                        onHymnClick = { hymn -> viewModel.openHymn(hymn) }
                                    )
                                }

                                BottomTab.FAVORITES -> {
                                    FavoritesScreen(
                                        favorites = favoriteHymns,
                                        onToggleFavorite = { hymn -> viewModel.toggleFavorite(hymn) },
                                        onHymnClick = { hymn -> viewModel.openHymn(hymn) }
                                    )
                                }

                                BottomTab.SETTINGS -> {
                                    SettingsScreen(
                                        fontSize = fontSize,
                                        onIncreaseFontSize = { viewModel.increaseFontSize() },
                                        onDecreaseFontSize = { viewModel.decreaseFontSize() },
                                        onResetFontSize = { viewModel.resetFontSize() },
                                        onSyncHymns = { viewModel.refreshHymns() },
                                        isLoadingHymns = isLoading,
                                        onSeed400 = { viewModel.seedAdditional400() },
                                        isUploading400 = isUploading400,
                                        uploadStatusMessage = uploadStatusMessage,
                                        totalHymns = hymns.size,
                                        onChurchAction = { actionId ->
                                            when (actionId) {
                                                "donate" -> viewModel.navigateTo(AppScreen.DonatePage)
                                                "contact" -> viewModel.navigateTo(AppScreen.ContactPage)
                                                "about" -> viewModel.navigateTo(AppScreen.AboutPage)
                                                "website" -> activeChurchSheet = "website"
                                                "blog" -> activeChurchSheet = "blog"
                                            }
                                        }
                                    )
                                }
                            }

                            // Show Church Info Sheet when active
                            activeChurchSheet?.let { sheetType ->
                                ChurchInfoBottomSheet(
                                    type = sheetType,
                                    onDismiss = { activeChurchSheet = null }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
