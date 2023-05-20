package com.esprit.smarktgo.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.repository.OrderRepository
import com.esprit.smarktgo.view.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainViewModel(private val mainActivity: MainActivity): ViewModel() {

    lateinit var userId:String
    var orderId = ""
    private val orderRepository = OrderRepository()

    init {
        getOrder()
    }

    fun getOrder() {
        try {
            val googleSignIn = GoogleSignIn.getLastSignedInAccount(mainActivity)
            userId = if(googleSignIn!=null) {
                googleSignIn.email!!
            } else FirebaseAuth.getInstance().currentUser?.phoneNumber!!

            viewModelScope.launch {
                val result = orderRepository.get(userId)

                result?.let {
                    orderId = result.id
                }
            }
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }

}