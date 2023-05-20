package com.esprit.smarktgo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.esprit.smarktgo.R
import com.esprit.smarktgo.model.Supermarket
import com.esprit.smarktgo.utils.RetrofitInstance.BASE_URL
import com.esprit.smarktgo.view.FavoritesFragment
import com.esprit.smarktgo.view.HomeFragment

class SupermarketAdapter(private val homeFragment: HomeFragment?, private val favoritesFragment: FavoritesFragment?) : RecyclerView.Adapter<SupermarketViewHolder>() {

    private var list = ArrayList<Supermarket>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Supermarket>) {
        this.list = list as ArrayList<Supermarket>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupermarketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.supermarket_item, parent, false)
        return SupermarketViewHolder(view)
    }


    override fun onBindViewHolder(holder: SupermarketViewHolder, position: Int) {
        Glide.with(holder.itemView).load(BASE_URL+"img/" + list[position].image).into(holder.imageV)
        holder.nameTV.text = list[position].name
        holder.addressTV.text = list[position].address
        holder.itemView.setOnClickListener {
            val id = list[position].id
            val image = list[position].image
            val name= list[position].name
            val description =list[position].description
            val address =list[position].address
            val location = list[position].location
            homeFragment?.let {
                homeFragment.navigateToSupermarketActivity(id,name,description,address,image,location)
            }?: favoritesFragment?.navigateToSupermarketActivity(id,name,description,address,image,location)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}

class SupermarketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageV : ImageView
    val nameTV : TextView
    val addressTV : TextView

    init {
        imageV = itemView.findViewById(R.id.supermarketImage)
        nameTV = itemView.findViewById(R.id.supermarketName)
        addressTV = itemView.findViewById(R.id.supermarketAddress)
    }

}