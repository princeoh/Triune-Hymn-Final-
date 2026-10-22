package com.example

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tv = TextView(this)
        tv.text = "HELLO PRINCE - PURE ANDROID"
        tv.textSize = 24f
        tv.setPadding(60, 400, 60, 60)
        setContentView(tv)
    }
}
