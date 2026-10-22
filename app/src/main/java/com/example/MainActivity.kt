package com.example

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // NO setTheme! NO Compose! Just plain text!
        super.onCreate(savedInstanceState)
        
        val textView = TextView(this)
        textView.text = "HELLO PRINCE!\n\nIf you see this, basic app works!\n\nNow we know crash is in Theme or Compose.\n\nNext step: Fix Theme_TriuneHymn"
        textView.textSize = 20f
        textView.setPadding(50, 200, 50, 50)
        
        setContentView(textView)
    }
}
