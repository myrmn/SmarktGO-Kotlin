package com.esprit.smarktgo.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.esprit.smarktgo.R
import com.esprit.smarktgo.adapter.ItemAdapter
import com.esprit.smarktgo.databinding.ActivityItemsBinding
import com.esprit.smarktgo.model.Item
import com.esprit.smarktgo.viewmodel.ItemsViewModel
import com.google.android.material.snackbar.Snackbar


class ItemsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityItemsBinding
    private lateinit var itemViewModel: ItemsViewModel
    private lateinit var itemAdapter: ItemAdapter
    val orderDialog = OrderDialog(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)
        binding = ActivityItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val category= intent.getStringExtra("category")
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.toolbar.setTitle(category)

        val supermarketId= intent.getStringExtra("supermarketId")
        prepareRecyclerView()
        itemViewModel = ItemsViewModel(this)
        itemViewModel.getItems(category!!, supermarketId!!)

        itemViewModel.observeItemsLiveData().observe(this, Observer { list ->
            itemAdapter.setList(list)
        })

    }

    private fun prepareRecyclerView() {
        itemAdapter = ItemAdapter(this,null)
        binding.rvItems.apply {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)
        }
    }

    fun showImage(){
        binding.imageView.isVisible = true
        binding.noItemsTV.isVisible = true
    }

    fun addToCart(item: Item,quantity: Int)
    {
        itemViewModel.addToCart(item,quantity)
    }

    fun showSuccessSnackBar(text: String)
    {
        Snackbar.make(findViewById(R.id.itemsConstraintLayout),text, Snackbar.LENGTH_LONG).show()
    }


}