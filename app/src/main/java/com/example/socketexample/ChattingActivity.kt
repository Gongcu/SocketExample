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
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_chatting.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
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
        mSocket.on("join", onJoin)
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
        api.getChat(room,uid).enqueue(object: Callback<List<ChatItem>> {
            override fun onResponse(call: Call<List<ChatItem>>, response: Response<List<ChatItem>>) {
                Log.d("res",response!!.body()!!.toString())
                var list = response!!.body()!!
                for(i in list.indices){
                    if(list[i].uid==uid){
                        list[i].viewType=ChatAdapter.MY_CHAT
                    }
                    else
                        list[i].viewType=ChatAdapter.OTHER_CHAT
                }
                adapter.setItem(list)
            }
            override fun onFailure(call: Call<List<ChatItem>>, t: Throwable) {
                Log.d("tt",t.message)
            }
        })
    }


    //새 메시지가(new message 이벤트) 도착할 시 뷰를 업데이트 하는 리스너
    private val onNewMessage = Emitter.Listener { args ->
        ioScope.launch {
            val data :JSONObject?= args[0] as? JSONObject

            val id: String
            val sender_uid: String
            val name: String
            val image: String
            val message: String
            val time: String
            val count: Int

            id = data!!.getString("id")
            sender_uid = data!!.getString("uid")
            name = data!!.getString("name")
            image = data!!.getString("image")
            message = data!!.getString("message")
            time = data!!.getString("createdAt")
            count = data!!.getInt("count")

            val chatItem = ChatItem(id,sender_uid,name,image,message,count,time)
            if(sender_uid == uid)
                chatItem.viewType= ChatAdapter.MY_CHAT
            else
                chatItem.viewType= ChatAdapter.OTHER_CHAT

            //view update
            adapter.addItem(chatItem)
        }
    }
    private val onJoin = Emitter.Listener { args ->
        ioScope.launch {
            val chatList = ArrayList<ChatItem>()
            val data :JSONArray?= args[0] as? JSONArray
            var id: String
            var sender_uid: String
            var name: String
            var image: String
            var message: String
            var time: String
            var count: Int
            for(i in 0 until data!!.length()){
                val item = data.getJSONObject(i)
                id = item.getString("id")
                sender_uid = item.getString("uid")
                name = item.getString("name")
                image = item.getString("image")
                message = item.getString("message")
                time = item.getString("createdAt")
                count = item.getInt("count")
                val chatItem = ChatItem(id,sender_uid,name,image,message,count,time)
                if(sender_uid == uid)
                    chatItem.viewType= ChatAdapter.MY_CHAT
                else
                    chatItem.viewType= ChatAdapter.OTHER_CHAT
                chatList.add(chatItem)
            }

            adapter.countUpdate(chatList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket.disconnect()
        mSocket.off("new message", onNewMessage)
    }
}