package com.example.socketexample.model

import com.google.gson.annotations.SerializedName

data class ChatItem(
    val id: String,
    val userId: String,
    val name: String,
    val image: String,
    val message: String,
    var count: Int,
    @SerializedName("createdAt")val time: String,
    var viewType: Int = 0
)
