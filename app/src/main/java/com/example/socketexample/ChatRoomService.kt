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
    ): Call<List<ChatroomGetter>>

    @POST("/chatroom")
    fun addChatRoom(
        @Body chatroom: ChatroomSetter,
    ): Call<ChatroomSetter>

    @GET("/chat/{chatroomId}")
    fun getChat(
        @Path("chatroomId") chatroomId:String
    ): Call<List<ChatItem>>

    @POST("/chat")
    fun sendChat(
        @Body chat: Chat,
    ): Call<Chat>
}
