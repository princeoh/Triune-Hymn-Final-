package com.example

import android.os.Bundle
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

class MainActivity : ComponentActivity() {

    private val repository by lazy { HymnRepository(applicationContext) }
    private val viewModel: HymnViewModel by viewModels { HymnViewModelFactory(repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_TriuneHymn)
        super.onCreate(savedInstanceState)
        // Firebase removed for offline build - will re-add with real key later
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}
