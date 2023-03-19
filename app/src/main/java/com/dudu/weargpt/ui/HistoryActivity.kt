package com.dudu.weargpt.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dudu.weargpt.R
import com.dudu.weargpt.adapter.HistoryAdapter
import com.dudu.weargpt.bean.Conversation
import org.litepal.LitePal
import org.litepal.crud.LitePalSupport
import org.litepal.extension.findAll

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val recyclerView:RecyclerView = findViewById(R.id.history_list)
        val conversationList = LitePal.findAll<Conversation>()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = HistoryAdapter(conversationList.reversed())

    }
}