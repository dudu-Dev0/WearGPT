package com.dudu.weargpt.ui

import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dudu.weargpt.R
import com.dudu.weargpt.adapter.HistoryAdapter
import com.dudu.weargpt.bean.Conversation
import com.dudu.weargpt.view.DeleteDialog
import org.litepal.LitePal
import org.litepal.extension.findAll

class HistoryActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var conversationList: List<Conversation>
    lateinit var deleteDialog: DeleteDialog
    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    refresh()
                }

                else -> {}
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recyclerView = findViewById(R.id.history_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        refresh()

    }

    private fun refresh() {
        conversationList = LitePal.findAll<Conversation>()
        deleteDialog = DeleteDialog(this)
        recyclerView.adapter = HistoryAdapter(conversationList.reversed(), this, deleteDialog)
        deleteDialog.setOnDismissListener {
            refresh()
        }
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }
}

