package com.esprit.smarktgo.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.model.*
import com.esprit.smarktgo.repository.ReviewRepository
import com.esprit.smarktgo.repository.SupermarketRepository
import com.esprit.smarktgo.repository.UserRepository
import com.esprit.smarktgo.view.SupermarketActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class SupermarketViewModel(supermarketActivity: SupermarketActivity): ViewModel()  {

    var categoriesLiveData = MutableLiveData<List<String>>()
    var favorites = MutableLiveData<ArrayList<String>>()
    lateinit var userId: String
    val mActivity = supermarketActivity
    var isFavorite = MutableLiveData<Boolean>()
    val supermarketRepository = SupermarketRepository()
    val reviewRepository = ReviewRepository()
    var reviewsLiveData = MutableLiveData<List<Review>>()
    var userLiveData = MutableLiveData<User>()

    private val userRepository: UserRepository = UserRepository()

    init {
        getCategories()
        isFavorite()
        getSupermarketReviews(mActivity.supermarketId)
        getUserInfo()
    }

    private fun getCategories() {
        try {
            viewModelScope.launch {
                val result = supermarketRepository.getCategories()
                result.let {
                    categoriesLiveData.value = result
                }

            }

        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }

    private fun isFavorite() {
        try {
            val googleSignIn = GoogleSignIn.getLastSignedInAccount(mActivity.baseContext)
            userId = if(googleSignIn!=null) {
                googleSignIn.email!!
            } else FirebaseAuth.getInstance().currentUser?.phoneNumber!!

            viewModelScope.launch {
                val result = supermarketRepository.isFavorite(IsFavoriteBody(mActivity.supermarketId,userId))
                result.let {
                    favorites.value = result as ArrayList<String>?
                    isFavorite.value = result?.contains(userId)
                }

            }

        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }

    fun addFavorite() {
        favorites.value?.add(userId)
        isFavorite.value = !isFavorite.value!!
        addRemoveFavorite()
    }

    fun removeFavorite() {
        favorites.value?.remove(userId)
        isFavorite.value = !isFavorite.value!!
        addRemoveFavorite()
    }

    fun addRemoveFavorite(){
        try {

            viewModelScope.launch {
                supermarketRepository.addRemoveFavorite(AddRemoveFavorite(mActivity.supermarketId, favorites.value!!))
            }

        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }
    fun submitReview(
        title: String,
        username: String,
        userId: String,
        supermarketName: String,
        supermarketId: String,
        description: String,
        rating: Float,
        function: () -> Unit
    ){
        try {
     viewModelScope.launch {
      reviewRepository.submitReview(addReview(title,username,userId,supermarketName,supermarketId,description,rating))
         function()
     }
        }  catch(e:ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
        }

     fun getSupermarketReviews(supermarketId:String){
         try {
             viewModelScope.launch {
             val reviews=    reviewRepository.getSupermarketReviews(supermarketId)
                 reviews.let {
                     reviewsLiveData.value = reviews
                 }
             }

         }  catch(e:ApiException) {
             Log.w(ContentValues.TAG, e.statusCode.toString())

         }
     }
    private fun getUserInfo() {
        val user = User(userId, "", wallet = 0.0)
        try {
            viewModelScope.launch {
                val data = userRepository.signIn(user)
                data?.let {
                    userLiveData.value = data
                }
            }
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }
    fun observeUser(): LiveData<User> = userLiveData

    fun observeReviews() : LiveData<List<Review>> = reviewsLiveData

    fun observeCategoriesLiveData() : LiveData<List<String>> = categoriesLiveData

    fun observeIsFavoriteLiveData() : LiveData<Boolean> = isFavorite

}