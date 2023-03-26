package com.dudu.weargpt.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dudu.weargpt.R
import com.dudu.weargpt.utils.StartActivity

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        findViewById<TextView>(R.id.copyright_tv).setOnClickListener {
            StartActivity<OpenSourceActivity> { }
        }
    }
}