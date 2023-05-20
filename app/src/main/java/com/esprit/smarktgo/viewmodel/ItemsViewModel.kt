package com.esprit.smarktgo.viewmodel

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.R
import com.esprit.smarktgo.model.Chat
import com.esprit.smarktgo.model.Item
import com.esprit.smarktgo.model.Order
import com.esprit.smarktgo.repository.ItemRepository
import com.esprit.smarktgo.repository.OrderRepository
import com.esprit.smarktgo.view.ItemsActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mapbox.maps.extension.style.expressions.dsl.generated.switchCase
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ItemsViewModel(itemsActivity: ItemsActivity): ViewModel()  {

    var itemsLiveData = MutableLiveData<List<Item>>()
    @SuppressLint("StaticFieldLeak")
    private val mActivity = itemsActivity
    lateinit var userId: String


    fun getItems(category:String,supermarketId:String) {
        try {
            val itemRepository = ItemRepository()

            viewModelScope.launch {
                val result = itemRepository.getAllBySupermarketIdAndCategory(category,supermarketId)
                result.let {
                    itemsLiveData.value = result
                }
                if(result!!.isEmpty())
                    mActivity.showImage()
            }

        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }

    fun observeItemsLiveData() : LiveData<List<Item>> = itemsLiveData

    fun addToCart(item: Item, quantity : Int) {
        try {
            val googleSignIn = GoogleSignIn.getLastSignedInAccount(mActivity.baseContext)
            userId = if(googleSignIn!=null) {
                googleSignIn.email!!
            } else FirebaseAuth.getInstance().currentUser?.phoneNumber!!


            val orderRepository = OrderRepository()
            var items = ArrayList<Item>()
            item.quantity = quantity
            items.add(item)
            val order = Order(userId,items)

            viewModelScope.launch {
                val result = orderRepository.addToCart(order)
                if(result!=null)
                {
                    when ( result.code()) {
                        200 -> {
                            mActivity.showSuccessSnackBar(mActivity.getString(R.string.added_to_cart))
                        }201 -> {
                            mActivity.showSuccessSnackBar(mActivity.getString(R.string.added_to_cart))
                            addChat(result.body()!!.id)
                        }
                        400 -> mActivity.showSuccessSnackBar( mActivity.getString(R.string.already_have_orders))
                    }
                }
                

            }
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }
    
    private fun addChat(orderId: String)
    {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val current = LocalDateTime.now().format(formatter)

        val db = Firebase.firestore
        val chat = Chat(current,"","","",orderId,0)

        db.collection("chats")
            .add(chat)
            .addOnSuccessListener { documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}") }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }


}