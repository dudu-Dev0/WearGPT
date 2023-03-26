package com.dudu.weargpt.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.tencent.bugly.crashreport.CrashReport
import org.litepal.LitePal

class GPTApplication() :Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context:Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        LitePal.initialize(context)
        CrashReport.initCrashReport(context)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }
}