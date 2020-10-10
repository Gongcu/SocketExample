package com.example.socketexample.model

import com.google.gson.annotations.SerializedName

data class ChatItem(
    @SerializedName("User") val user: User,
    val message: String,
    @SerializedName("createdAt")val time: String,
    var viewType: Int = 0
)
