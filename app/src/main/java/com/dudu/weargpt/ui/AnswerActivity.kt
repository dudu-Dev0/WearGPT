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
import com.dudu.weargpt.utils.GPTApplication.Companion.context
import com.dudu.weargpt.utils.MyGrammarLocator
import com.dudu.weargpt.utils.OpenAiInterceptor
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.theokanning.openai.OpenAiApi
import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.completion.chat.ChatMessage
import com.theokanning.openai.service.OpenAiService
import com.theokanning.openai.service.OpenAiService.defaultRetrofit
import io.noties.markwon.Markwon
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.glide.GlideImagesPlugin
import io.noties.markwon.syntax.Prism4jThemeDarkula
import io.noties.markwon.syntax.SyntaxHighlightPlugin
import io.noties.prism4j.Prism4j
import io.noties.prism4j.annotations.PrismBundle
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


@PrismBundle(includeAll = true)
class AnswerActivity : AppCompatActivity() {
    lateinit var key : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

        val question = intent.getStringExtra("question")
        Log.e(question, question!!)
        findViewById<TextView>(R.id.question_tv).text = question

        key = getSharedPreferences("settings", MODE_PRIVATE).getString("api_key","")!!

        thread{
            val mapper = myObjectMapper()
            val client: OkHttpClient = myClient(key)!!
                .newBuilder()
                .build();
            val retrofit = defaultRetrofit(client, mapper)
            val api = retrofit.create(OpenAiApi::class.java)
            val service = OpenAiService(api)

            val completionRequest = ChatCompletionRequest.builder()
                .messages(mutableListOf(ChatMessage("user",question)))
                .model("gpt-3.5-turbo")
                .build()
            val answer = service.createChatCompletion(completionRequest).choices[0].message.content
            runOnUiThread{
                val prism4j = Prism4j(MyGrammarLocator())
                val markwon: Markwon = Markwon.builder(context) // automatically create Glide instance
                    .usePlugin(GlideImagesPlugin.create(context)) // use supplied Glide instance
                    .usePlugin(GlideImagesPlugin.create(Glide.with(context))) // if you need more control
                    .usePlugin(SyntaxHighlightPlugin.create(prism4j,Prism4jThemeDarkula.create(0)))
                    .usePlugin(GlideImagesPlugin.create(object : GlideImagesPlugin.GlideStore {
                        override fun load(@NonNull drawable: AsyncDrawable): RequestBuilder<Drawable> {
                            return Glide.with(context).load(drawable.getDestination())
                        }

                        override fun cancel(target: Target<*>) {
                            Glide.with(context).clear(target)
                        }
                    }))
                    .build()
                markwon.setMarkdown(findViewById<TextView>(R.id.answer_tv),answer)
                findViewById<FrameLayout>(R.id.loading_frame).visibility = View.GONE
            }
        }
    }
    private fun myClient(token: String?): OkHttpClient? {
        return OkHttpClient.Builder()
            .addInterceptor(OpenAiInterceptor(token))
            .connectionPool(ConnectionPool(5, 1, TimeUnit.SECONDS))
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    private fun myObjectMapper(): ObjectMapper? {
        val mapper = ObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        mapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        return mapper
    }

}