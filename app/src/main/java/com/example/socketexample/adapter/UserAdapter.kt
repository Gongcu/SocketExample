package com.example.socketexample.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socketexample.view.MainActivity
import com.example.socketexample.R
import com.example.socketexample.model.User

class UserAdapter(private val context: Context) : RecyclerView.Adapter<UserAdapter.ItemViewHolder>() {
    private val list = ArrayList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return ItemViewHolder(view)

    }

    override fun onBindViewHolder(holder: UserAdapter.ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list:List<User>){
        this.list.addAll(list)
        notifyDataSetChanged()
        Log.d("list",list.toString())
    }

    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
        private val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        fun bind(item: User){
            val url = MainActivity.IP+"image/"+item.image
            nameTextView.text=item.name;
            Glide.with(context).load(url).into(imageView)
            itemView.setOnClickListener {
                val intent = Intent(context, MainActivity::class.java)
                Log.d("uid",list.toString())
                intent.putExtra("uid",item.id)
                intent.putExtra("name",item.name)
                context.startActivity(intent)
            }
        }

    }
}