package com.esprit.smarktgo.viewmodel

import android.content.ContentValues
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Handler
import android.text.BoringLayout
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.model.Supermarket
import com.esprit.smarktgo.repository.SupermarketRepository
import com.esprit.smarktgo.view.HomeFragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch

import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.view.FavoritesFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth

class FavoritesViewModel(favoritesFragment: FavoritesFragment): ViewModel() {

    private val mFragment = favoritesFragment
    lateinit var userId: String
    private var supermarketsLiveData = MutableLiveData<List<Supermarket>>()

    init {
        getFavorites()
    }

    fun getFavorites() {
        try {
            supermarketsLiveData.value = ArrayList()
            val supermarketRepository = SupermarketRepository()

            val googleSignIn = GoogleSignIn.getLastSignedInAccount(mFragment.requireContext())
            userId = if(googleSignIn!=null) {
                googleSignIn.email!!
            } else FirebaseAuth.getInstance().currentUser?.phoneNumber!!

            viewModelScope.launch {
                val result = supermarketRepository.getFavorites(User(userId,"",0.0))

                result.let {
                    supermarketsLiveData.value = result
                }
                if(result!!.isEmpty())
                    mFragment.showImage()
            }
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }

    fun observeSupermarketsLiveData() : LiveData<List<Supermarket>>  = supermarketsLiveData

}

