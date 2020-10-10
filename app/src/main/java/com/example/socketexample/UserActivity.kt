package com.example.socketexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
                if (response.body() != null) {
                    adapter.setList(response.body()!!)
                }
            }
            override fun onFailure(call: Call<List<User>>, t: Throwable) {}
        })
    }
}