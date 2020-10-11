package com.example.socketexample

import com.example.socketexample.model.*
import retrofit2.Call
import retrofit2.http.*

interface ChatRoomService {
    @GET("/user")
    fun getUser(
    ): Call<List<User>>

    @GET("/chatroom/{club_id}")
    fun getChatRoom(
        @Path("club_id") club_id:String
    ): Call<List<Chatroom>>

    @POST("/chatroom")
    fun addChatRoom(
        @Body chatroom: Chatroom,
    ): Call<Chatroom>

    @GET("/chatroom/enter/{chatroomId}/{uid}")
    fun getChat(
        @Path("chatroomId") chatroomId:String,
        @Path("uid") uid:String,
        ): Call<List<ChatItem>>

    @DELETE("/chat/read/{uid}/{chatroomId}")
    fun readChat(
        @Path("chatroomId") chatroomId:String,
        @Path("uid") uid:String,
    ): Call<ChatItem>

    @DELETE("/chatroom/leave/{uid}/{chatroomId}")
    fun leaveRoom(
        @Path("chatroomId") chatroomId:String,
        @Path("uid") uid:String,
    ): Call<Void>

    @POST("/chat")
    fun sendChat(
        @Body chat: Chat
    ): Call<Chat>
}
