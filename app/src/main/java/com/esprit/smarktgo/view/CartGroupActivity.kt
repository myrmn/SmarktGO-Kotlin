package com.esprit.smarktgo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.esprit.smarktgo.R
import com.esprit.smarktgo.adapter.UserAdapter
import com.esprit.smarktgo.databinding.ActivityCartGroupBinding
import com.esprit.smarktgo.viewmodel.CartGroupViewModel
import com.google.android.material.snackbar.Snackbar

class CartGroupActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCartGroupBinding
     lateinit var cartGroupViewModel: CartGroupViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_group)
        binding = ActivityCartGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        val userId = intent.getStringExtra("userId")

        cartGroupViewModel = CartGroupViewModel(this, userId!!)
        prepareRecyclerView()
        cartGroupViewModel.observeUsersLiveData().observe(this, Observer { users ->
            userAdapter.setList(users)
        })

    }

    private fun prepareRecyclerView() {
        userAdapter = UserAdapter(this)
        binding.rvUsers.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL ,false)
        }
    }

    fun showSnackBar(text: String)
    {
        Snackbar.make(findViewById(R.id.cartGroupConstraintLayout),text, Snackbar.LENGTH_LONG).show()
    }
}