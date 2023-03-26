package com.dudu.weargpt.bean

import org.litepal.crud.LitePalSupport
import java.io.Serializable

class Conversation : LitePalSupport(), Serializable {
    var question = ""
    var answer = ""
    var time = ""
    var tick = 0L
    var id = 0L
}