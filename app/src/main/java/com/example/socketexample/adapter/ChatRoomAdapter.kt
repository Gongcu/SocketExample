package com.example.socketexample.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socketexample.view.ChattingActivity
import com.example.socketexample.R
import com.example.socketexample.model.Chatroom

class ChatRoomAdapter(private val context: Context,private val userId: String,private val name: String) : RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {
    private val list = ArrayList<Chatroom>()

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

    fun addItem(item: Chatroom){
        val chatroomGetter = Chatroom(item.id,item.name,item.clubId)
        list.add(chatroomGetter)
        notifyDataSetChanged()
        Log.d("notifydatasetchage",list.toString())
    }
    fun setList(list:List<Chatroom>){
        this.list.addAll(list)
        notifyDataSetChanged()
        Log.d("notifydatasetchage",list.toString())
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val rootView = itemView
        private val nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)

        fun bind(item: Chatroom){
            nameTextView.text=item.name
            rootView.setOnClickListener {
                val intent = Intent(context, ChattingActivity::class.java)
                intent.putExtra("userId",userId)
                intent.putExtra("chatroomId",item.id)
                intent.putExtra("room_name",item.name)
                intent.putExtra("name",name)
                context.startActivity(intent)
            }
        }
    }


}