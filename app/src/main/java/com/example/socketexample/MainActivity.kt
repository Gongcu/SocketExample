package com.example.socketexample

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private val CLUB_ID = "5f76b1830ba3354340163d6d"
    private val USER1_ID = "5f4fa0073fabdf5f285bdc08"
    private val USER2_ID = "5f4fa75a35adb13fa8823de6"
    private val retrofit = RetrofitAPI.getInstnace()
    private val api = retrofit.create(ChatRoomService::class.java)
    private val adapter:ChatRoomAdapter by lazy{
        ChatRoomAdapter(this)
    }
    private val userlist = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chatroomRecyclerView.adapter = ChatRoomAdapter(this)

        userlist.add(USER1_ID)
        userlist.add(USER2_ID)

        getChatroom()

        addChatRoomBtn.setOnClickListener {
            //showDialog()
        }

    }

    fun showDialog(){
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_chatroom, null)
        val name = dialogView.findViewById<EditText>(R.id.chatroomEditText)
        builder.setView(dialogView)

        builder.setView(dialogView)
            .setPositiveButton("확인") { dialogInterface, i ->
                addChatroom(Chatroom("",name.text.toString(),CLUB_ID,userlist))
            }.show()
    }

    fun addChatroom(chatroom: Chatroom){
        api.addChatRoom(chatroom).enqueue(object : Callback<Chatroom> {
            override fun onResponse(call: Call<Chatroom>, response: Response<Chatroom>) {
                if (response.body() != null)
                    adapter.addItem(response.body()!!)
            }

            override fun onFailure(call: Call<Chatroom>, t: Throwable) {
            }
        })
    }

    fun getChatroom(){
        api.getChatRoom(CLUB_ID).enqueue(object : Callback<List<Chatroom>> {
            override fun onResponse(
                call: Call<List<Chatroom>>,
                response: Response<List<Chatroom>>
            ) {
                if (response.body() != null) {
                    adapter.setList(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Chatroom>>, t: Throwable) {}
        })
    }
}