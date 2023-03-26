package com.dudu.weargpt.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.dudu.weargpt.R


/**
 * Created by ruancw on 2018/6/7.
 * 自定义的带圆角的对话框
 */
class DeleteDialog(context: Context) : AlertDialog(context) {
    private lateinit var tvCancel: TextView
    private lateinit var tvConfirm: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_delete)
        //设置背景透明，不然会出现白色直角问题
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
        //初始化布局控件
        initView()
        //确定和取消按钮的事件监听
        initEvent()
        //设置参数必须在show之后，不然没有效果
        window?.attributes = getWindow()?.getAttributes()
    }

    /**
     * 初始化布局文件及设置参数
     */
    private fun initView() {
        tvCancel = findViewById(R.id.cancel_tv)!!
        tvConfirm = findViewById(R.id.confirm_tv)!!
    }

    /**
     * 确定及取消点击事件
     */
    private fun initEvent() {
        tvCancel.setOnClickListener {
            dismiss()
        }
        tvConfirm.setOnClickListener {
            confirmListener.onConfirmClick()
            dismiss()
        }
    }

    private lateinit var confirmListener: ConfirmListener

    /**
     * 设置确定按钮的监听
     * @param confirmListener
     */
    fun setConfirmListener(confirmListener: ConfirmListener) {
        this.confirmListener = confirmListener
    }

    /**
     * 确定按钮点击的监听接口
     */
    interface ConfirmListener {
        fun onConfirmClick()
    }
}