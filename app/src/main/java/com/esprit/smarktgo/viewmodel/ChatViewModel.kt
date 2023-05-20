package com.esprit.smarktgo.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.model.Chat
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.repository.OrderRepository
import com.esprit.smarktgo.repository.UserRepository
import com.esprit.smarktgo.view.ChatFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatViewModel(private val mFragment: ChatFragment): ViewModel() {

    private val db = Firebase.firestore
    lateinit var  chats : ArrayList<Chat>
    var messageOrder = 0
    lateinit var userId : String
    lateinit var orderId : String
    lateinit var userName : String
    private val orderRepository = OrderRepository()
    private val userRepository = UserRepository()

    init {
        getOrder()
    }

    private fun getOrder() {
        try {

            val googleSignIn = GoogleSignIn.getLastSignedInAccount(mFragment.requireContext())
            userId = if(googleSignIn!=null) {
                googleSignIn.email!!
            } else FirebaseAuth.getInstance().currentUser?.phoneNumber!!

            getUserName()

            viewModelScope.launch {
                val result = orderRepository.get(userId)

                result?.let {
                    orderId = result.id
                    mFragment.prepareRecyclerView(userId)
                    mFragment.showChat()
                    listenToChat()
                }?: mFragment.showNoOrder()
            }
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }

    private fun getUserName() {
        val user = User(userId, "", wallet = 0.0)
        try {
            viewModelScope.launch {
                val data = userRepository.signIn(user)
                data?.let {
                    userName = data.fullName
                }
            }
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }

    private fun listenToChat()
    {
        db.collection("chats")
            .whereEqualTo("orderId", orderId)
            .orderBy("order")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.e("TAG", "Listen failed.", e)
                    return@addSnapshotListener
                }
                chats = ArrayList<Chat>()
                for (doc in value!!) {
                    if(doc.getString("message")!="")
                    {
                        val chat = Chat(doc.getString("dateTime")!!, doc.getString("userId")!!, doc.getString("message")!!,
                            doc.getString("userName")!!, doc.getString("orderId")!!,doc.getField("order")!!)
                        chats.add(chat)
                        if(chat.order>messageOrder)
                            messageOrder=chat.order
                    }
                }
                mFragment.chatAdapter.setList(chats)
                mFragment.rvMessages.scrollToPosition(chats.size - 1);
            }
    }

    fun sendMessage(message: String,userName:String)
    {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val current = LocalDateTime.now().format(formatter)

        val db = Firebase.firestore
        val chat = Chat(current,userId,message.trim(),userName,orderId,messageOrder+1)

        db.collection("chats")
            .add(chat)
            .addOnSuccessListener {
                mFragment.rvMessages.scrollToPosition(chats.size - 1)
            }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error adding document", e) }

    }

}