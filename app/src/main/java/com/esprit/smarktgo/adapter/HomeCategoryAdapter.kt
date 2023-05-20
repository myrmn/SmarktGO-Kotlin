package com.esprit.smarktgo.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.esprit.smarktgo.R
import com.esprit.smarktgo.model.HomeCategory
import com.esprit.smarktgo.model.Item
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.view.CartGroupActivity
import com.esprit.smarktgo.view.HomeFragment


class HomeCategoryAdapter(val mFragment: HomeFragment) : RecyclerView.Adapter<HomeCategoryViewHolder>() {

    private var list = ArrayList<HomeCategory>()

    @SuppressLint("NotifyDataSetChanged")
    fun init() {
        this.list.add(HomeCategory(name = mFragment.requireContext().getString(R.string.fruits), image = R.drawable.fruits))
        this.list.add(HomeCategory(name = mFragment.requireContext().getString(R.string.vegetables), image = R.drawable.vegetables))
        this.list.add(HomeCategory(name = mFragment.requireContext().getString(R.string.dairy_products), image = R.drawable.dairy_products))
        this.list.add(HomeCategory(name = mFragment.requireContext().getString(R.string.baby_food), image = R.drawable.baby_food))
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_category_item, parent, false)
        return HomeCategoryViewHolder(view)
    }


    override fun onBindViewHolder(holder: HomeCategoryViewHolder, position: Int) {
        holder.nameTV.text = list[position].name
        Glide.with(mFragment).asGif().load(list[position].image).into(holder.imageV)

    }

    override fun getItemCount(): Int {
        return list.size
    }

}

class HomeCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nameTV : TextView
    val imageV : ImageView

    init {
        imageV = itemView.findViewById(R.id.homeCategoryImage)
        nameTV = itemView.findViewById(R.id.homeCategoryName)
    }

}