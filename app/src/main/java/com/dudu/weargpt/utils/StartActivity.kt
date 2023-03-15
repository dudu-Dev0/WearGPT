package com.dudu.weargpt.utils

import android.content.Context
import android.content.Intent
import android.os.Build

inline fun <reified T> StartActivity(context: Context = GPTApplication.context, block: Intent.() -> Unit) {
    val intent = Intent(context, T::class.java)
    intent.block()
    if ((Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}