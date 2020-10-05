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
    private val retrofit = RetrofitAPI.getInstance()
    private val api = retrofit.create(ChatRoomService::class.java)
    private val adapter:ChatRoomAdapter by lazy{
        ChatRoomAdapter(this)
    }
    private val userList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userList.add(USER1_ID)
        userList.add(USER2_ID)
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
                addChatroom(ChatroomSetter("",name.text.toString(),CLUB_ID,ArrayList()))
            }.show()
    }

    fun addChatroom(chatroom: ChatroomSetter){
        api.addChatRoom(chatroom).enqueue(object : Callback<ChatroomSetter> {
            override fun onResponse(call: Call<ChatroomSetter>, response: Response<ChatroomSetter>) {
                if (response.body() != null) {
                    adapter.addItem(response.body()!!)
                }
            }
            override fun onFailure(call: Call<ChatroomSetter>, t: Throwable) {
            }
        })
    }

    fun getChatroom(){
        api.getChatRoom(CLUB_ID).enqueue(object : Callback<List<ChatroomGetter>> {
            override fun onResponse(call: Call<List<ChatroomGetter>>,response: Response<List<ChatroomGetter>>) {
                if (response.body() != null) {
                    adapter.setList(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<ChatroomGetter>>, t: Throwable) {}
        })
    }

    companion object{
        const val IP = "http://211.176.83.66:3000/"
        const val CLUB_ID = "5f76b1830ba3354340163d6d"
        const val USER1_ID = "5f4fa0073fabdf5f285bdc08"
        const val USER2_ID = "5f4fa75a35adb13fa8823de6"

    }
}