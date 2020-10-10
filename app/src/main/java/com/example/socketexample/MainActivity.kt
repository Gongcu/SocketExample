package com.example.socketexample

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.socketexample.adapter.ChatRoomAdapter
import com.example.socketexample.model.Chatroom
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var uid: String
    private lateinit var name: String
    private val retrofit = RetrofitAPI.getInstance()
    private val api = retrofit.create(ChatRoomService::class.java)
    private val adapter: ChatRoomAdapter by lazy{
        ChatRoomAdapter(this,uid,name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uid= intent.extras!!.getString("uid")!!
        name= intent.extras!!.getString("name")!!

        chatroomRecyclerView.adapter = adapter


        getChatroom()

        addChatRoomBtn.setOnClickListener {
            showDialog()
        }

    }

    fun showDialog(){
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_chatroom, null)
        val name = dialogView.findViewById<EditText>(R.id.chatroomEditText)
        builder.setView(dialogView)

        builder.setView(dialogView)
            .setPositiveButton("확인") { dialogInterface, i ->
                addChatroom(Chatroom("",name.text.toString(),CLUB_ID))
            }.show()
    }

    fun addChatroom(chatroom: Chatroom){
        api.addChatRoom(chatroom).enqueue(object : Callback<Chatroom> {
            override fun onResponse(call: Call<Chatroom>, response: Response<Chatroom>) {
                if (response.body() != null) {
                    adapter.addItem(response.body()!!)
                }
            }
            override fun onFailure(call: Call<Chatroom>, t: Throwable) {
            }
        })
    }

    fun getChatroom(){
        api.getChatRoom(CLUB_ID).enqueue(object : Callback<List<Chatroom>> {
            override fun onResponse(call: Call<List<Chatroom>>, response: Response<List<Chatroom>>) {
                if (response.body() != null) {
                    adapter.setList(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Chatroom>>, t: Throwable) {}
        })
    }

    companion object{
        const val IP = "http://211.176.83.66:3000/"
        const val CLUB_ID = "1"

    }
}