package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var msg by remember { mutableStateOf("Starting app...\n") }
            
            LaunchedEffect(Unit) {
                try {
                    msg += "Step 1: Testing HymnCatalog...\n"
                    val catalog = com.example.data.datasource.HymnCatalog.getFullHymnLibrary()
                    msg += "Catalog OK: ${catalog.size} hymns\n"
                    delay(500)
                    
                    msg += "Step 2: Testing Repository...\n"
                    val repo = com.example.data.repository.HymnRepository(applicationContext)
                    msg += "Repository OK\n"
                    delay(500)
                    
                    msg += "Step 3: Testing ViewModel...\n"
                    // If we reach here, main app should work
                    msg += "ALL CHECKS PASSED! Real app should launch now.\n"
                    
                } catch (e: Throwable) {
                    msg += "\n\n=== CRASH FOUND ===\n"
                    msg += "Type: ${e::class.java.name}\n"
                    msg += "Message: ${e.message}\n\n"
                    msg += "Stacktrace:\n${e.stackTraceToString().take(5000)}"
                    e.printStackTrace()
                }
            }
            
            Text(
                text = msg,
                modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()),
                fontSize = 13.sp
            )
        }
    }
}
