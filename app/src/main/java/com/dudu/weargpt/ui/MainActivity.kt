package com.dudu.weargpt.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dudu.weargpt.R
import com.dudu.weargpt.utils.StartActivity
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<MaterialCardView>(R.id.new_competition_card).setOnClickListener {
            StartActivity<CompetitionActivity> {  }
        }
        findViewById<MaterialCardView>(R.id.history_card).setOnClickListener {
            StartActivity<HistoryActivity> {  }
        }
        findViewById<MaterialCardView>(R.id.settings_card).setOnClickListener {
            StartActivity<SettingsActivity> {  }
        }
    }
}