package com.dudu.weargpt.ui

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.dudu.weargpt.R
import com.dudu.weargpt.utils.makeToast
import com.dudu.weargpt.utils.open
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class SetKeyActivitiy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_key_activitiy)

        val keyText :EditText= findViewById(R.id.key_text_view)
        val saveButton :ExtendedFloatingActionButton = findViewById(R.id.save_key_button)

        if (keyText.text.toString() == "") saveButton.hide() else saveButton.show()

        keyText.addTextChangedListener {
                if (keyText.text.toString() == "") saveButton.hide() else saveButton.show()
            }
        saveButton.setOnClickListener{
            getSharedPreferences("settings",Context.MODE_PRIVATE).open {
                putString("api_key",keyText.text.toString())
            }
            makeToast("设置成功")
            finish()
        }
    }

}