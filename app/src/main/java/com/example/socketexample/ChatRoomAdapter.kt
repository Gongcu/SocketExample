package com.example.socketexample

import android.content.Context
import android.content.Intent
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.content.res.ComplexColorCompat.inflate
import androidx.recyclerview.widget.RecyclerView

class ChatRoomAdapter(private val context: Context) : RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {
    private val list = ArrayList<ChatroomGetter>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_chatroom, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatRoomAdapter.ViewHolder, position: Int) {
        holder.bind(list[position]);
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addItem(item:ChatroomSetter){
        val chatroomGetter = ChatroomGetter(item._id,item.name,item.club_id, ArrayList())
        list.add(chatroomGetter)
        notifyDataSetChanged()
        Log.d("notifydatasetchage",list.toString())
    }
    fun setList(list:List<ChatroomGetter>){
        this.list.addAll(list)
        notifyDataSetChanged()
        Log.d("notifydatasetchage",list.toString())
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val rootView = itemView
        private val nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)

        fun bind(item: ChatroomGetter){
            nameTextView.text=item.name
            rootView.setOnClickListener {
                showDialog(item)
            }
        }
        fun showDialog(item: ChatroomGetter){
            val builder = AlertDialog.Builder(context)
            val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_chatroom, null)
            val name = dialogView.findViewById<TextView>(R.id.nameTextView)
            val nickname = dialogView.findViewById<EditText>(R.id.chatroomEditText)
            name.text = "닉네임"
            nickname.hint ="채팅방에서 사용할 닉네임을 입력하세요"
            builder.setView(dialogView)

            builder.setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->
                    val intent = Intent(context,ChattingActivity::class.java)
                    intent.putExtra("room",item._id)
                    intent.putExtra("nickname",nickname.text.toString())
                    context.startActivity(intent)
                }.show()
        }
    }


}