package com.example.socketexample.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.socketexample.util.ChatRoomService
import com.example.socketexample.R
import com.example.socketexample.util.RetrofitAPI
import com.example.socketexample.adapter.UserAdapter
import com.example.socketexample.model.User
import kotlinx.android.synthetic.main.activity_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserActivity : AppCompatActivity() {
    private val retrofit = RetrofitAPI.getInstance()
    private val api = retrofit.create(ChatRoomService::class.java)
    private val adapter: UserAdapter by lazy{
        UserAdapter(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        userRecyclerView.adapter = adapter
        getUser()
    }
    private fun getUser(){
        api.getUser().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                Log.d("response",response.body().toString())
                if (response.body() != null) {
                    adapter.setList(response.body()!!)
                }
            }
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.d("response",t.stackTraceToString())
            }
        })
    }
}