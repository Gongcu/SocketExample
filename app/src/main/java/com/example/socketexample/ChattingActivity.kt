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
import kotlin.properties.Delegates


class ChattingActivity : AppCompatActivity() {
    private var roomNum by Delegates.notNull<Int>()
    private val mSocket = IO.socket("http://211.176.83.66:3000")
    private val ioScope = CoroutineScope(Dispatchers.Main)
    private val adapter:Adapter by lazy{
        Adapter(this)
    }

    //새 메시지가(new message 이벤트) 도착할 시 뷰를 업데이트 하는 리스너
    private val onNewMessage = Emitter.Listener { args ->
        ioScope.launch {
            val data = args[0] as? JSONObject
            Log.d("data",data.toString())

            val sender: String
            val message: String
            val count = 0

            sender = data!!.getString("sender")
            message = data!!.getString("message")
            val chatItem = ChatItem(sender, message, count)

            //view update
            adapter.addItem(chatItem)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)

        roomNum= intent.extras!!.getInt("room")

        mSocket.on("new message", onNewMessage)
        mSocket.connect() //room 번호 따라 다르게 연결하게 코드 변경 필요
        mSocket.emit("join", roomNum)//방 입장

        recyclerView.adapter = adapter

        sendBtn.setOnClickListener {
            attemptSend()
        }
    }

    private fun attemptSend() {
        val message: String = inputMessageTextView.text.toString()
        val jsonObject = JSONObject()
        jsonObject.put("room", roomNum)
        jsonObject.put("sender", "공채운")
        jsonObject.put("message", message)
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
}