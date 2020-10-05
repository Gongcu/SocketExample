package com.example.socketexample

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitAPI {
    private var instance : Retrofit? = null
    fun getInstance() : Retrofit {
        if(instance == null){
            instance = Retrofit.Builder()
                .baseUrl(MainActivity.IP)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return instance!!
    }
}