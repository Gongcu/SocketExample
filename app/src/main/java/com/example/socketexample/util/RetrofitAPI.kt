package com.example.socketexample.util

import com.example.socketexample.view.MainActivity
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