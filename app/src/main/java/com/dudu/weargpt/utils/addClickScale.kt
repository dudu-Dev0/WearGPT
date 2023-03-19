package com.dudu.weargpt.utils

import android.view.MotionEvent
import android.view.View

/**
 * 添加点击缩放效果
 */
fun View.addClickScale(scale: Float = 0.9f, duration: Long = 150) {
    this.setOnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                this.animate().scaleX(scale).scaleY(scale).setDuration(duration).start()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                this.animate().scaleX(1f).scaleY(1f).setDuration(duration).start()
            }
        }
        // 点击事件处理，交给View自身
        this.onTouchEvent(event)
    }
}