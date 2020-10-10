package com.example.socketexample.model

import com.google.gson.annotations.SerializedName

data class ChatItem(
    val uid: String,
    val name: String,
    val image: String,
    val message: String,
    val count: Int,
    @SerializedName("createdAt")val time: String,
    var viewType: Int = 0
)
