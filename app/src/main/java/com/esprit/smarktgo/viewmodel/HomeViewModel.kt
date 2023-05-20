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
import com.esprit.smarktgo.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth

class HomeViewModel(homeFragment: HomeFragment): ViewModel() {

    private val mFragment = homeFragment

    var supermarketsLiveData = MutableLiveData<List<Supermarket>>()
    private val fusedLocationProviderClient: FusedLocationProviderClient
    //private  var location =  MutableLiveData<Location>()
    //private  val locationManager: LocationManager
    lateinit var task : Task<Location>

    init {
        //locationManager = mFragment.context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(homeFragment.requireActivity())
        checkLocationPermission()
        getAll()
        mFragment.welcomeTV.text = mFragment.requireContext().getString(R.string.welcome)
        val googleSignIn = GoogleSignIn.getLastSignedInAccount(mFragment.requireContext())
        googleSignIn?.let{
            mFragment.welcomeTV.text = mFragment.requireContext().getString(R.string.welcome) + " " +  googleSignIn.displayName
        }
    }

    private fun getAll() {
        try {
            val supermarketRepository = SupermarketRepository()

            viewModelScope.launch {
                val result = supermarketRepository.getAll()
                result.let {
                    supermarketsLiveData.value = result
                }
            }
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }

    fun observeSupermarketsLiveData() : LiveData<List<Supermarket>>  = supermarketsLiveData

    //fun observeLocationLiveData() : LiveData<Location> = location

    private fun checkLocationPermission()
    {
        task = fusedLocationProviderClient.lastLocation

        if(ActivityCompat.checkSelfPermission(mFragment.requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mFragment.requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(mFragment.requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)
        }

        return
    }

    /*private fun checkLocation(){
        Handler().postDelayed({
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                task.addOnSuccessListener {
                    it?.let {
                        location.value=it
                    }
                }
            }
            checkLocation()
        },2500)
    }*/



}

