package com.esprit.smarktgo.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esprit.smarktgo.R
import com.esprit.smarktgo.adapter.ChatAdapter
import com.esprit.smarktgo.viewmodel.ChatViewModel
import com.google.android.material.textfield.TextInputEditText

class ChatFragment() : Fragment() {

    private lateinit var chatViewModel: ChatViewModel
    lateinit var chatAdapter: ChatAdapter
    lateinit var rvMessages: RecyclerView
    lateinit var sendIcon: ImageView
    lateinit var message: TextInputEditText

    lateinit var emptyCartImage: ImageView
    lateinit var emptyCartText: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_chat,container,false)

        rvMessages = view.findViewById(R.id.rv_messages)
        chatViewModel = ChatViewModel(this)

        sendIcon = view.findViewById(R.id.sendIcon)
        message = view.findViewById(R.id.message)
        emptyCartImage = view.findViewById(R.id.emptyCartIV)
        emptyCartText = view.findViewById(R.id.emptyCartTV)

        sendIcon.setOnClickListener {
            if(message.text!!.isNotEmpty())
            {
                val userName = chatViewModel.userName
                chatViewModel.sendMessage(message.text.toString(), userName)
                message.text!!.clear()
                rvMessages.scrollToPosition(chatViewModel.chats.size - 1);
            }
        }

        return view
    }


    fun prepareRecyclerView(userId:String) {
        chatAdapter = ChatAdapter(userId)
        rvMessages.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL ,false)
        }
    }

    fun showChat()
    {
        emptyCartImage.isVisible = false
        emptyCartText.isVisible = false
        message.isVisible = true
        sendIcon.isVisible = true
        rvMessages.isVisible = true
    }

    fun showNoOrder()
    {
        emptyCartImage.isVisible = true
        emptyCartText.isVisible = true
        message.isVisible = false
        sendIcon.isVisible = false
        rvMessages.isVisible = false
    }

}