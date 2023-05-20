package com.esprit.smarktgo.view

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esprit.smarktgo.R
import com.esprit.smarktgo.adapter.SupermarketAdapter
import com.esprit.smarktgo.model.Location
import com.esprit.smarktgo.viewmodel.FavoritesViewModel
import okhttp3.internal.notify


class FavoritesFragment : Fragment() {

    private lateinit var favoritesViewModel: FavoritesViewModel
    lateinit var rv: RecyclerView
    lateinit var imageV: ImageView
    lateinit var textV: TextView
    private lateinit var supermarketAdapter : SupermarketAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_favorites,container,false)
        rv = view.findViewById(R.id.rv_favorite_supermarkets)
        textV = view.findViewById(R.id.noFavoritesTV)
        imageV = view.findViewById(R.id.imageView)

        prepareRecyclerView()
        favoritesViewModel = FavoritesViewModel(this)
        favoritesViewModel.observeSupermarketsLiveData().observe(requireActivity(), Observer { list ->
            supermarketAdapter.setList(list)
        })
        return view
    }

    private fun prepareRecyclerView() {
        supermarketAdapter = SupermarketAdapter(null,this)
        rv.apply {
            adapter = supermarketAdapter
            layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL ,false)
        }
    }

    fun navigateToSupermarketActivity(id: String, name: String, description: String?, address: String?, image: String?,location: Location) {
        val intent = Intent(requireContext(), SupermarketActivity::class.java).apply {
            putExtra("supermarketId", id)
            putExtra("name", name)
            putExtra("description", description)
            putExtra("address", address)
            putExtra("image", image)
            putExtra("latitude", location.coordinates[0])
            putExtra("longitude", location.coordinates[1])
        }
        startActivity(intent)
    }

    fun showImage(){
        imageV.isVisible = true
        textV.isVisible = true
    }

    fun updateList()
    {
        favoritesViewModel.getFavorites()
    }

}