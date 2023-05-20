package com.esprit.smarktgo.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.esprit.smarktgo.R
import com.esprit.smarktgo.databinding.ActivityMainBinding
import com.esprit.smarktgo.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val favoritesFragment = FavoritesFragment()
    lateinit var mainViewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = MainViewModel(this)
        replaceFragment(HomeFragment())

        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId)
            {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.cart -> replaceFragment(CartFragment(this))
                R.id.chat -> replaceFragment(ChatFragment())
                R.id.favorites -> replaceFragment(favoritesFragment)
                R.id.profile -> replaceFragment(ProfileFragment())
            }
            true
        }

        binding.toolbar.setOnMenuItemClickListener {
            mainViewModel.getOrder()
            if (mainViewModel.orderId.isNotEmpty())
            when(it.itemId) {
                R.id.cartGroup -> {
                    val intent = Intent(this, CartGroupActivity::class.java).apply {
                        putExtra("userId",mainViewModel.userId)
                    }
                    startActivity(intent)
                }
            }
            else
                Snackbar.make(findViewById(R.id.mainActivityConstraintLayout),getString(R.string.empty_cart), Snackbar.LENGTH_LONG).show()
            true
        }

    }

    private fun replaceFragment(fragment: Fragment){
        val bundle = Bundle()
        fragment.arguments = bundle
        val fragmentManager =  supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
    }

    override fun onResume() {
        super.onResume()
        if(binding.bottomNav.selectedItemId == R.id.home)
            mainViewModel.getOrder()
        if(binding.bottomNav.selectedItemId == R.id.favorites)
            favoritesFragment.updateList()
        if(binding.bottomNav.selectedItemId == R.id.profile)
        {
            replaceFragment(ProfileFragment())
        }
    }
}