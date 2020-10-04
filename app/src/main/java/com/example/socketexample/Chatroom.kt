package com.example.socketexample

data class Chatroom(
    val _id: String,
    val name: String,
    val club_id: String,
    val participation_uid_list: ArrayList<String>
)