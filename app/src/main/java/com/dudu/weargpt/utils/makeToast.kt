package com.dudu.weargpt.utils

import android.widget.Toast

fun makeToast(text:String){
    Toast.makeText(GPTApplication.context,text,Toast.LENGTH_SHORT).show()
}