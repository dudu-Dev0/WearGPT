package com.dudu.weargpt.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.target.Target
import com.dudu.weargpt.R
import com.dudu.weargpt.bean.Conversation
import com.dudu.weargpt.utils.GPTApplication.Companion.context
import com.dudu.weargpt.utils.MyGrammarLocator
import com.dudu.weargpt.utils.makeToast
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.theokanning.openai.OpenAiHttpException
import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.completion.chat.ChatMessage
import com.theokanning.openai.service.OpenAiAPI
import com.theokanning.openai.service.OpenAiService
import com.theokanning.openai.service.OpenAiService.defaultClient
import com.theokanning.openai.service.OpenAiService.defaultRetrofit
import io.noties.markwon.Markwon
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.glide.GlideImagesPlugin
import io.noties.markwon.syntax.Prism4jThemeDarkula
import io.noties.markwon.syntax.SyntaxHighlightPlugin
import io.noties.prism4j.Prism4j
import io.noties.prism4j.annotations.PrismBundle
import okhttp3.OkHttpClient
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.Calendar
import kotlin.concurrent.thread


@PrismBundle(includeAll = true)
class AnswerActivity : AppCompatActivity() {
    lateinit var key: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

        var answer = ""
        var question = intent.getStringExtra("question")

        if (!intent.getBooleanExtra("isHistory", false)) {
            Log.e(question, question!!)
            findViewById<TextView>(R.id.question_tv).text = question

            key = getSharedPreferences("settings", MODE_PRIVATE).getString("api_key", "")!!

            thread {
                val mapper = myObjectMapper()
                val client: OkHttpClient = defaultClient(key, 500)
                val retrofit = defaultRetrofit(
                    client,
                    mapper,
                    getSharedPreferences("settings", MODE_PRIVATE).getString(
                        "base_url",
                        "https://api.dudu-dev.ml/api/"
                    )
                )
                val api = retrofit.create(OpenAiAPI::class.java)
                val service = OpenAiService(api)

                val completionRequest = ChatCompletionRequest.builder()
                    .messages(mutableListOf(ChatMessage("user", question)))
                    .model("gpt-3.5-turbo")
                    .build()
                try {
                    answer =
                        service.createChatCompletion(completionRequest).choices[0].message.content
                } catch (e: OpenAiHttpException) {
                    runOnUiThread {
                        makeToast(e.toString())
                    }
                } catch (e: SocketTimeoutException) {
                    runOnUiThread {
                        makeToast("连接超时")
                    }
                } catch (e: ConnectException) {
                    runOnUiThread {
                        Log.e("", e.message!!)
                        makeToast("无法连接至服务器，请尝试使用代理")
                    }
                }

                runOnUiThread {
                    val prism4j = Prism4j(MyGrammarLocator())
                    val markwon: Markwon =
                        Markwon.builder(context) // automatically create Glide instance
                            .usePlugin(GlideImagesPlugin.create(context)) // use supplied Glide instance
                            .usePlugin(GlideImagesPlugin.create(Glide.with(context))) // if you need more control
                            .usePlugin(
                                SyntaxHighlightPlugin.create(
                                    prism4j,
                                    Prism4jThemeDarkula.create(0)
                                )
                            )
                            .usePlugin(GlideImagesPlugin.create(object :
                                GlideImagesPlugin.GlideStore {
                                override fun load(@NonNull drawable: AsyncDrawable): RequestBuilder<Drawable> {
                                    return Glide.with(context).load(drawable.getDestination())
                                }

                                override fun cancel(target: Target<*>) {
                                    Glide.with(context).clear(target)
                                }
                            }))
                            .build()
                    markwon.setMarkdown(findViewById(R.id.answer_tv), answer)
                    val conversation = Conversation()
                    conversation.answer = answer
                    conversation.question = question
                    conversation.time = getNow()
                    conversation.tick = System.currentTimeMillis();
                    conversation.save()
                    findViewById<FrameLayout>(R.id.loading_frame).visibility = View.GONE
                }
            }
        } else {
            answer = intent.getStringExtra("answer")!!
            findViewById<FrameLayout>(R.id.loading_frame).visibility = View.GONE
            findViewById<TextView>(R.id.question_tv).text = question
            val prism4j = Prism4j(MyGrammarLocator())
            val markwon: Markwon = Markwon.builder(context) // automatically create Glide instance
                .usePlugin(GlideImagesPlugin.create(context)) // use supplied Glide instance
                .usePlugin(GlideImagesPlugin.create(Glide.with(context))) // if you need more control
                .usePlugin(SyntaxHighlightPlugin.create(prism4j, Prism4jThemeDarkula.create(0)))
                .usePlugin(GlideImagesPlugin.create(object : GlideImagesPlugin.GlideStore {
                    override fun load(@NonNull drawable: AsyncDrawable): RequestBuilder<Drawable> {
                        return Glide.with(context).load(drawable.getDestination())
                    }

                    override fun cancel(target: Target<*>) {
                        Glide.with(context).clear(target)
                    }
                }))
                .build()
            markwon.setMarkdown(findViewById(R.id.answer_tv), answer!!)
        }

    }

    private fun myObjectMapper(): ObjectMapper? {
        val mapper = ObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        mapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        return mapper
    }

    private fun getNow(): String {
        //if (android.os.Build.VERSION.SDK_INT >= 24){
        //return SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Date())
        //}else{
        var tms = Calendar.getInstance()
        return tms.get(Calendar.YEAR).toString() + "-" + tms.get(Calendar.MONTH)
            .toString() + "-" + tms.get(Calendar.DAY_OF_MONTH)
            .toString() + " " + tms.get(Calendar.HOUR_OF_DAY)
            .toString() + ":" + tms.get(Calendar.MINUTE).toString() + ":" + tms.get(Calendar.SECOND)
            .toString() + "." + tms.get(Calendar.MILLISECOND).toString()
        //}

    }

}