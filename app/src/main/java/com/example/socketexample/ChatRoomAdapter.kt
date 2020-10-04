package com.example.socketexample

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatRoomAdapter(private val context: Context) : RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {
    private val list = ArrayList<Chatroom>()

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

    fun addItem(item:Chatroom){
        list.add(item)
        notifyDataSetChanged()
        Log.d("notifydatasetchage",list.toString())
    }
    fun setList(list:List<Chatroom>){
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val rootView = itemView
        private val nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)

        fun bind(item: Chatroom){
            nameTextView.text=item.name;
            rootView.setOnClickListener {
                val intent = Intent(context,ChattingActivity::class.java)
                intent.putExtra("room",item._id)
                context.startActivity(intent)
            }
        }
    }
}