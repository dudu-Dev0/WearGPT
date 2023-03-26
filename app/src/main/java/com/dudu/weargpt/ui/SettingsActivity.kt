package com.dudu.weargpt.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dudu.weargpt.R
import com.dudu.weargpt.utils.StartActivity
import com.google.android.material.card.MaterialCardView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<MaterialCardView>(R.id.set_api_key_card).setOnClickListener {
            StartActivity<SetKeyActivitiy> { }
        }

        findViewById<MaterialCardView>(R.id.set_api_server).setOnClickListener {
            StartActivity<ChooseServerActivity> { }
        }
    }
}