package com.dudu.weargpt.ui

import android.os.Bundle
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.dudu.weargpt.R
import com.dudu.weargpt.utils.open
import com.google.android.material.radiobutton.MaterialRadioButton

class ChooseServerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_server)

        if (getSharedPreferences("settings", MODE_PRIVATE).getString(
                "base_url",
                ""
            ) == "https://api.openai.com/"
        ) findViewById<MaterialRadioButton>(R.id.rb_official).isChecked = true
        if (getSharedPreferences("settings", MODE_PRIVATE).getString(
                "base_url",
                ""
            ) == "https://api.dudu-dev.ml/api/"
        ) findViewById<MaterialRadioButton>(R.id.rb_proxy).isChecked = true

        findViewById<RadioGroup>(R.id.server_radio_group).setOnCheckedChangeListener { group, checkedId ->
            val url = if (checkedId == R.id.rb_proxy) "https://api.dudu-dev.ml/api/"
            else "https://api.openai.com/"
            getSharedPreferences("settings", MODE_PRIVATE).open {
                putString("base_url", url)
            }
            finish()
        }
    }
}