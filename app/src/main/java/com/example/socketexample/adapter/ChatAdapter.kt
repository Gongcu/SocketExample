package com.example.socketexample.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socketexample.R
import com.example.socketexample.model.ChatItem

class ChatAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val list = ArrayList<ChatItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            MY_CHAT -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_my_chat, parent, false)
                return MyChatViewHolder(view)
            }
            OTHER_CHAT ->{
                val view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false)
                return OtherChatViewHolder(view)
            }
            else ->{//cannot reach this
                val view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false)
                return OtherChatViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is MyChatViewHolder){
            holder.bind(list[position])
        }else if(holder is OtherChatViewHolder){
            holder.bind(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(list.isNotEmpty())
            list[position].viewType
        else
            super.getItemViewType(position)
    }

    fun addItem(item: ChatItem){
        list.add(item)
        notifyDataSetChanged()
        Log.d("notifydatasetchage",list.toString())
    }
    fun setItem(list: List<ChatItem>){
        this.list.addAll(list)
        notifyDataSetChanged()
        Log.d("notifydatasetchage",list.toString())
    }

    inner class OtherChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
        private val messageTextView = itemView.findViewById<TextView>(R.id.messageTextView)
        private val timeTextView = itemView.findViewById<TextView>(R.id.timeTextView)


        fun bind(item: ChatItem){
            nameTextView.text=item.user.name;
            messageTextView.text=item.message
            timeTextView.text = item.time
        }
    }

    inner class MyChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val messageTextView = itemView.findViewById<TextView>(R.id.messageTextView)
        private val timeTextView = itemView.findViewById<TextView>(R.id.timeTextView)

        fun bind(item: ChatItem){
            messageTextView.text=item.message
            timeTextView.text = item.time
        }
    }

    companion object{
        const val MY_CHAT = 0
        const val OTHER_CHAT = 1
    }
}