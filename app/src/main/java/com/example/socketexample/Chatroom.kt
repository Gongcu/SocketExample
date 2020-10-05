package com.example.socketexample

data class ChatroomSetter(
    val _id: String,
    val name: String,
    val club_id: String,
    val participation_uid_list: ArrayList<String>
)

data class ChatroomGetter(
    val _id: String,
    val name: String,
    val club_id: String,
    val participation_uid_list: ArrayList<ChatUser>
)

data class ChatUser(
    val _id: String,
    val name: String,
    val image: String
)