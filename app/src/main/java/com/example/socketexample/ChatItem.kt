package com.example.socketexample

data class ChatItem(
    val sender: String,
    val message: String,
    val time: String,
    var viewType: Int = 0
)