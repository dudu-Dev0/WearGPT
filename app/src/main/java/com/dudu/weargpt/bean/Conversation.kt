package com.dudu.weargpt.bean

import org.litepal.crud.LitePalSupport

class Conversation: LitePalSupport() {
    var question = ""
    var answer = ""
    var time = ""
    var tick = 0L
}