package com.example.socketexample

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val context: Context) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    private val list = ArrayList<ChatItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatAdapter.ViewHolder, position: Int) {
        holder.bind(list[position]);
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addItem(item:ChatItem){
        list.add(item)
        notifyDataSetChanged()
        Log.d("notifydatasetchage",list.toString())
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
        private val messageTextView = itemView.findViewById<TextView>(R.id.messageTextView)
        private val countTextView = itemView.findViewById<TextView>(R.id.countTextView)

        fun bind(item: ChatItem){
            nameTextView.text=item.sender;
            messageTextView.text=item.message
            countTextView.text=item.count.toString()
        }
    }
}