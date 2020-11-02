package com.example.socketexample.util

import com.example.socketexample.model.*
import retrofit2.Call
import retrofit2.http.*

interface ChatRoomService {
    @GET("/user")
    fun getUser(
    ): Call<List<User>>

    @GET("/chatroom/{clubId}/{userId}")
    fun getChatRoom(
        @Path("clubId") clubId:String,
        @Path("userId") userId:String
    ): Call<List<Chatroom>>

    @POST("/chatroom")
    fun addChatRoom(
        @Body chatroom: Chatroom,
    ): Call<Chatroom>

    @GET("/chatroom/{chatroomId}/{userId}/enter")
    fun getChat(
        @Path("chatroomId") chatroomId:String,
        @Path("userId") userId:String,
        ): Call<List<ChatItem>>

    @DELETE("/chatroom/{chatroomId}/{userId}/leave")
    fun leaveRoom(
        @Path("chatroomId") chatroomId:String,
        @Path("userId") userId:String,
    ): Call<Void>

    @POST("/chatroom/{chatroomId}/chat")
    fun sendChat(
        @Path("chatroomId") chatroomId:String,
        @Body chat: Chat
    ): Call<Chat>
}
