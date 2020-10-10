package com.example.socketexample.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.socketexample.ChattingActivity
import com.example.socketexample.R
import com.example.socketexample.model.ChatroomGetter
import com.example.socketexample.model.ChatroomSetter

class ChatRoomAdapter(private val context: Context,private val uid: String,private val name: String) : RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {
    private val list = ArrayList<ChatroomGetter>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_chatroom, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position]);
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addItem(item: ChatroomSetter){
        val chatroomGetter = ChatroomGetter(item.id,item.name,item.club_id)
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
                val intent = Intent(context, ChattingActivity::class.java)
                intent.putExtra("uid",uid)
                intent.putExtra("room",item.id)
                intent.putExtra("room_name",item.name)
                intent.putExtra("name",name)
                context.startActivity(intent)
            }
        }
    }


}