package com.esprit.smarktgo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.esprit.smarktgo.R
import com.esprit.smarktgo.model.Chat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatAdapter(val userId: String) : RecyclerView.Adapter<ChatViewHolder>() {

    private var list = ArrayList<Chat>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Chat>) {
        this.list = list as ArrayList<Chat>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return ChatViewHolder(view)
    }


    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        val date = list[position].dateTime.split("\\s".toRegex())[0]
        val time = list[position].dateTime.split("\\s".toRegex()).last()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val current = LocalDateTime.now().format(formatter)

        if(date!=current.toString())
            holder.dateTimeTV.text = list[position].dateTime
        else
            holder.dateTimeTV.text = time

        if(list[position].userId==userId)
        {
            holder.fromUserNameTV.text = list[position].userName
            holder.fromMessageTV.text = list[position].message
            holder.fromUserNameTV.isVisible = true
            holder.fromMessageTV.isVisible = true
            holder.toUserNameTV.isVisible = false
            holder.toMessageTV.isVisible = false

        }
        else
        {
            holder.toUserNameTV.text = list[position].userName
            holder.toMessageTV.text = list[position].message
            holder.toUserNameTV.isVisible = true
            holder.toMessageTV.isVisible = true
            holder.fromUserNameTV.isVisible = false
            holder.fromMessageTV.isVisible = false
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val dateTimeTV : TextView
    val fromUserNameTV : TextView
    val fromMessageTV : TextView
    val toUserNameTV : TextView
    val toMessageTV : TextView

    init {
        dateTimeTV = itemView.findViewById(R.id.dateTime)
        fromUserNameTV = itemView.findViewById(R.id.fromUserName)
        fromMessageTV = itemView.findViewById(R.id.fromMessage)
        toUserNameTV = itemView.findViewById(R.id.toUserName)
        toMessageTV = itemView.findViewById(R.id.toMessage)
    }

}