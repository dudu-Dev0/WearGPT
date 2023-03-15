package com.dudu.weargpt.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.dudu.weargpt.R
import com.dudu.weargpt.utils.StartActivity
import com.dudu.weargpt.utils.makeToast
import com.dudu.weargpt.utils.open
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.theokanning.openai.completion.CompletionRequest
import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.completion.chat.ChatMessage
import com.theokanning.openai.service.OpenAiService
import kotlin.concurrent.thread
import kotlin.time.Duration
import kotlin.time.toJavaDuration


class CompetitionActivity : AppCompatActivity() {
    lateinit var key : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_competition)
        //val service = OpenAiService("your_token")
        //val completionRequest = CompletionRequest.builder()
        //    .prompt("Somebody once told me the world is gonna roll me")
        //    .model("ada")
        //   .echo(true)
        //    .build()
        //service.createCompletion(completionRequest).getChoices().forEach(System.out::println)

        key = getSharedPreferences("settings", MODE_PRIVATE).getString("api_key","")!!

        val questionText : EditText = findViewById(R.id.question_text_view)
        val askButton : ExtendedFloatingActionButton = findViewById(R.id.ask_button)

        if (questionText.text.toString() == "") askButton.hide() else askButton.show()

        questionText.addTextChangedListener {
            if (questionText.text.toString() == "") askButton.hide() else askButton.show()
        }

        askButton.setOnClickListener{
            StartActivity<AnswerActivity> {
                putExtra("question",questionText.text.toString())
            }
            questionText.setText("")
        }
            /* thread{
                val service = OpenAiService(key,Duration.parse("60s").toJavaDuration())
                val completionRequest = ChatCompletionRequest.builder()
                    .messages(mutableListOf(ChatMessage("user",questionText.text.toString())))
                    .model("gpt-3.5-turbo")
                    .build()
                val answer = service.createChatCompletion(completionRequest).choices[0].message.content
                runOnUiThread{
                    makeToast(answer)

                }
                }
            }*/

        }
    }