package com.dudu.weargpt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dudu.weargpt.R
import com.dudu.weargpt.bean.Conversation
import com.dudu.weargpt.ui.AnswerActivity
import com.dudu.weargpt.utils.StartActivity
import com.dudu.weargpt.utils.addClickScale

class HistoryAdapter(val historyList: List<Conversation>) :RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){
    inner class ViewHolder(view:View) :RecyclerView.ViewHolder(view){
        val item :LinearLayout = view.findViewById(R.id.history_list_item)
        val questionText :TextView = view.findViewById(R.id.item_question_tv)
        val timeText :TextView = view.findViewById(R.id.item_time_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_list_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = historyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val conversation = historyList[position]

        holder.item.addClickScale()
        holder.item.setOnClickListener {
            StartActivity<AnswerActivity> {
                putExtra("question",conversation.question)
                putExtra("answer",conversation.answer)
                putExtra("isHistory",true)
            }
        }
        holder.timeText.text = conversation.time
        holder.questionText.text = conversation.question
    }


}