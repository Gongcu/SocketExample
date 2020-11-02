package com.example.socketexample.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.socketexample.util.ChatRoomService
import com.example.socketexample.R
import com.example.socketexample.util.RetrofitAPI
import com.example.socketexample.adapter.ChatAdapter
import com.example.socketexample.model.Chat
import com.example.socketexample.model.ChatItem
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
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
    private val list = ArrayList<ChatItem>()
    private lateinit var userId: String
    private lateinit var chatroomId: String
    private lateinit var name: String
    private val mSocket = IO.socket(MainActivity.IP)
    private val ioScope = CoroutineScope(Dispatchers.Main)
    private val adapter: ChatAdapter by lazy{
        ChatAdapter(this)
    }
    private val data:JSONObject = JSONObject()
    private val retrofit = RetrofitAPI.getInstance()
    private val api = retrofit.create(ChatRoomService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)

        userId= intent.extras!!.getString("userId")!!
        chatroomId= intent.extras!!.getString("chatroomId")!!
        name= intent.extras!!.getString("name")!!

        data.put("chatroomId",chatroomId)
        data.put("userId",userId)

        val room_name= intent.extras!!.getString("room_name")!!
        chatroomNameTextView.text=room_name


        recyclerView.adapter = adapter

        getChatting()

        sendBtn.setOnClickListener {
            attemptSend()
        }
    }

    private fun attemptSend() {
        val message: String = inputMessageTextView.text.toString()
        api.sendChat(chatroomId,Chat(userId,message,chatroomId)).enqueue(object: Callback<Chat>{
            override fun onResponse(call: Call<Chat>, response: Response<Chat>) {
                Log.d("send",response.body().toString())
                //chatroom=null 응답값으로 오는 chatroom값 어차피 필요없어서 그렇게 처리함. 근데 보낼때는 필수
            }

            override fun onFailure(call: Call<Chat>, t: Throwable) {
            }
        })
        inputMessageTextView.setText("")
    }

    private fun getChatting() {
        api.getChat(chatroomId,userId).enqueue(object: Callback<List<ChatItem>> {
            override fun onResponse(call: Call<List<ChatItem>>, response: Response<List<ChatItem>>) {
                Log.d("res",response!!.body()!!.toString())
                list.addAll(response.body()!!)
                for(i in list.indices){
                    if(list[i].userId==userId){
                        list[i].viewType=ChatAdapter.MY_CHAT
                    }
                    else
                        list[i].viewType=ChatAdapter.OTHER_CHAT
                }
                adapter.setList(list)
                recyclerView.scrollToPosition(adapter.itemCount - 1)

                //읽음 처리
                
            }
            override fun onFailure(call: Call<List<ChatItem>>, t: Throwable) {
                Log.d("tt",t.message)
            }
        })
    }


    //새 메시지가(new message 이벤트) 도착할 시 읽고 뷰에 업데이트
    private val onNewMessage = Emitter.Listener { args ->
        ioScope.launch {
            val item:JSONObject = args[0] as JSONObject;
            val chatId = item.getString("id")
            val sendUserId= item.getString("userId")
            val name = item.getString("name")
            val image = item.getString("image")
            val message = item.getString("message")
            val time = item.getString("createdAt")
            val count = item.getInt("count")

            val chatItem = ChatItem(chatId,sendUserId,name,image,message,count,time)
            if(sendUserId == userId)
                chatItem.viewType= ChatAdapter.MY_CHAT
            else
                chatItem.viewType= ChatAdapter.OTHER_CHAT
            adapter.addItem(chatItem)
            recyclerView.scrollToPosition(adapter.itemCount - 1)
        }
    }
    private val onJoin = Emitter.Listener { args ->
        ioScope.launch {
            val chatList = ArrayList<ChatItem>()
            val data :JSONArray?= args[0] as? JSONArray
            var chatId: String
            var sendUserId: String
            var name: String
            var image: String
            var message: String
            var time: String
            var count: Int

            for(i in 0 until data!!.length()){
                val item = data.getJSONObject(i)
                chatId = item.getString("id")
                sendUserId = item.getString("userId")
                name = item.getString("name")
                image = item.getString("image")
                message = item.getString("message")
                time = item.getString("createdAt")
                count = item.getInt("count")
                Log.d("count", "count:"+count)
                val chatItem = ChatItem(chatId,sendUserId,name,image,message,count,time)
                if(sendUserId == userId)
                    chatItem.viewType= ChatAdapter.MY_CHAT
                else
                    chatItem.viewType= ChatAdapter.OTHER_CHAT
                chatList.add(chatItem)
            }
            adapter.setList(chatList)
        }
    }


    override fun onResume(){
        super.onResume()
        mSocket.on("new message", onNewMessage)
        mSocket.on("join", onJoin)
        mSocket.connect()
        mSocket.emit("join", data)//방 입장
    }
    override fun onPause() {
        super.onPause()
        api.leaveRoom(chatroomId, userId).enqueue(object:Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {}
            override fun onFailure(call: Call<Void>, t: Throwable) {}
        })
        adapter.setList(ArrayList())
        mSocket.disconnect()
        mSocket.off("new message", onNewMessage)
        mSocket.off("join", onJoin)

    }
}