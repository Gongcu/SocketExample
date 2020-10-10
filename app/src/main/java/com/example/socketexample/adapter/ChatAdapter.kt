package com.example.socketexample.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.socketexample.MainActivity
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
        private val countTextView = itemView.findViewById<TextView>(R.id.countTextView)
        private val profileImageView = itemView.findViewById<ImageView>(R.id.profileImageView)

        fun bind(item: ChatItem){
            val url = MainActivity.IP+"image/"+item.image
            nameTextView.text=item.name
            messageTextView.text=item.message
            timeTextView.text = item.time
            if(item.count!=0){
                countTextView.text=item.count.toString()
            }
            Glide.with(context).load(url).apply(RequestOptions.bitmapTransform(RoundedCorners(15))).into(profileImageView).onLoadFailed(context.getDrawable(R.drawable.ic_baseline_person_24))
        }
    }

    inner class MyChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val messageTextView = itemView.findViewById<TextView>(R.id.messageTextView)
        private val timeTextView = itemView.findViewById<TextView>(R.id.timeTextView)
        private val countTextView = itemView.findViewById<TextView>(R.id.countTextView)

        fun bind(item: ChatItem){
            messageTextView.text=item.message
            timeTextView.text = item.time
            if(item.count!=0){
                countTextView.text=item.count.toString()
            }
        }
    }

    companion object{
        const val MY_CHAT = 0
        const val OTHER_CHAT = 1
    }
}