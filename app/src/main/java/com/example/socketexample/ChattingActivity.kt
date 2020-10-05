package com.example.socketexample

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import kotlinx.android.synthetic.main.activity_chatting.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class ChattingActivity : AppCompatActivity() {
    private lateinit var room: String
    private lateinit var nickname: String
    private val mSocket = IO.socket(MainActivity.IP)
    private val ioScope = CoroutineScope(Dispatchers.Main)
    private val adapter:ChatAdapter by lazy{
        ChatAdapter(this)
    }
    private val sdf = SimpleDateFormat("HH:mm")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)

        room= intent.extras!!.getString("room")!!
        nickname= intent.extras!!.getString("nickname")!!

        mSocket.on("new message", onNewMessage)
        mSocket.connect() //room 번호 따라 다르게 연결하게 코드 변경 필요
        mSocket.emit("join", room)//방 입장

        recyclerView.adapter = adapter

        sendBtn.setOnClickListener {
            attemptSend()
        }
    }

    private fun attemptSend() {
        val date = Date(System.currentTimeMillis())
        val time = sdf.format(date)

        val message: String = inputMessageTextView.text.toString()
        val jsonObject = JSONObject()
        jsonObject.put("room", room)
        jsonObject.put("sender", nickname)
        jsonObject.put("message", message)
        jsonObject.put("time", time)
        if (TextUtils.isEmpty(message)) {
            return
        }
        //이벤트 발생
        mSocket.emit("new message", jsonObject)
        inputMessageTextView.setText("")
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket.disconnect()
        mSocket.off("new message", onNewMessage)
    }
    //새 메시지가(new message 이벤트) 도착할 시 뷰를 업데이트 하는 리스너
    private val onNewMessage = Emitter.Listener { args ->
        ioScope.launch {
            val data = args[0] as? JSONObject
            Log.d("data",data.toString())

            val sender: String
            val message: String
            val time: String

            sender = data!!.getString("sender")
            message = data!!.getString("message")
            time = data!!.getString("time")

            val chatItem = ChatItem(sender, message, time)
            if(sender == nickname)
                chatItem.viewType=ChatAdapter.MY_CHAT
            else
                chatItem.viewType=ChatAdapter.OTHER_CHAT

            //view update
            adapter.addItem(chatItem)
        }
    }

    //새 메시지가(new message 이벤트) 도착할 시 뷰를 업데이트 하는 리스너
    private val onNewUser = Emitter.Listener { args ->
        ioScope.launch {
            val data = args[0] as? JSONObject
            Log.d("data",data.toString())

            val nickname: String = data!!.getString("nickname")

            //view update
            //adapter.addItem(chatItem)
        }
    }

}