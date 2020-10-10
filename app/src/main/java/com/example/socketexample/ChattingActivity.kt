package com.example.socketexample

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.socketexample.adapter.ChatAdapter
import com.example.socketexample.model.Chat
import com.example.socketexample.model.ChatItem
import com.example.socketexample.model.User
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import kotlinx.android.synthetic.main.activity_chatting.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChattingActivity : AppCompatActivity() {
    private lateinit var uid: String
    private lateinit var room: String
    private lateinit var name: String
    private val mSocket = IO.socket(MainActivity.IP)
    private val ioScope = CoroutineScope(Dispatchers.Main)
    private val adapter: ChatAdapter by lazy{
        ChatAdapter(this)
    }

    private val retrofit = RetrofitAPI.getInstance()
    private val api = retrofit.create(ChatRoomService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)

        uid= intent.extras!!.getString("uid")!!
        room= intent.extras!!.getString("room")!!
        name= intent.extras!!.getString("name")!!

        val room_name= intent.extras!!.getString("room_name")!!
        chatroomNameTextView.text=room_name


        mSocket.on("new message", onNewMessage)
        mSocket.connect() //room 번호 따라 다르게 연결하게 코드 변경 필요
        mSocket.emit("join", room)//방 입장

        recyclerView.adapter = adapter

        getChatting()

        sendBtn.setOnClickListener {
            attemptSend()
        }
    }

    private fun attemptSend() {
        val message: String = inputMessageTextView.text.toString()
        api.sendChat(Chat(uid,message,room)).enqueue(object: Callback<Chat>{
            override fun onResponse(call: Call<Chat>, response: Response<Chat>) {
                Log.d("send",response.body().toString())
            }

            override fun onFailure(call: Call<Chat>, t: Throwable) {
            }
        })
        inputMessageTextView.setText("")
    }

    private fun getChatting() {
        api.getChat(room).enqueue(object: Callback<List<ChatItem>> {
            override fun onResponse(call: Call<List<ChatItem>>, response: Response<List<ChatItem>>) {
                var list = response!!.body()!!
                for(i in list.indices){
                    Log.d("data",list[i].user.id+","+uid)
                    if(list[i].user.id==uid){
                        list[i].viewType=ChatAdapter.MY_CHAT
                    }
                    else
                        list[i].viewType=ChatAdapter.OTHER_CHAT
                }
                adapter.setItem(list)
            }
            override fun onFailure(call: Call<List<ChatItem>>, t: Throwable) {
            }
        })
    }


    //새 메시지가(new message 이벤트) 도착할 시 뷰를 업데이트 하는 리스너
    private val onNewMessage = Emitter.Listener { args ->
        ioScope.launch {
            val data :JSONObject?= args[0] as? JSONObject

            val sender_uid: String
            val name: String
            val image: String
            val message: String
            val time: String

            sender_uid = data!!.getString("uid")
            name = data!!.getJSONObject("User").getString("name")
            image = data!!.getJSONObject("User").getString("image")
            message = data!!.getString("message")
            time = data!!.getString("createdAt")

            val chatItem = ChatItem(User(sender_uid,name,image), message, time)
            if(sender_uid == uid)
                chatItem.viewType= ChatAdapter.MY_CHAT
            else
                chatItem.viewType= ChatAdapter.OTHER_CHAT

            //view update
            adapter.addItem(chatItem)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket.disconnect()
        mSocket.off("new message", onNewMessage)
    }
}