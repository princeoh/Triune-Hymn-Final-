package com.example

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this)
        textView.text = "HELLO PRINCE! If you see this, we won!"
        textView.textSize = 22f
        textView.setPadding(50, 300, 50, 50)
        setContentView(textView)
    }
}
