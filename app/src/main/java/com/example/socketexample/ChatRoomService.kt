package com.example.socketexample

import retrofit2.Call
import retrofit2.http.*

interface ChatRoomService {
    @GET("/chatroom/{club_id}")
    fun getChatRoom(
        @Path("club_id") club_id:String
    ): Call<List<ChatroomGetter>>

    @POST("/chatroom")
    fun addChatRoom(
        @Body chatroom: ChatroomSetter,
    ): Call<ChatroomSetter>
}