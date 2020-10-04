package com.example.socketexample

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ChatRoomService {
    @GET("/chatroom/{club_id}")
    fun getChatRoom(
        @Path("club_id") club_id:String
    ): Call<List<Chatroom>>

    @POST("/chatroom")
    fun addChatRoom(
        @Body chatroom: Chatroom,
    ): Call<Chatroom>
}