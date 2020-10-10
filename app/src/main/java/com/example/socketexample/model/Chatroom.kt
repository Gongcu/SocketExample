package com.example.socketexample.model

data class ChatroomSetter(
    val id: String,
    val name: String,
    val club_id: String,
    val participation_uid_list: ArrayList<String>
)

data class ChatroomGetter(
    val id: String,
    val name: String,
    val club_id: String,
)

data class ChatUser(
    val _id: String,
    val name: String,
    val image: String
)