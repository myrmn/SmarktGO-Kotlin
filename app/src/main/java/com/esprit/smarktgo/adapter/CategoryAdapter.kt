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
import com.esprit.smarktgo.utils.RetrofitInstance.BASE_URL
import com.esprit.smarktgo.view.SupermarketActivity

class CategoryAdapter(val mActivity: SupermarketActivity) : RecyclerView.Adapter<CategoryViewHolder>() {

    private var list = ArrayList<String>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<String>) {
        this.list = list as ArrayList<String>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }


    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Glide.with(holder.itemView).load(BASE_URL+"img/" + list[position] +".jpeg").into(holder.imageV)
        holder.nameTV.text = list[position]
        holder.itemView.setOnClickListener {
            val category = list[position]
            mActivity.navigateToItemsActivity(category)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}

class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nameTV : TextView
    val imageV : ImageView

    init {
        imageV = itemView.findViewById(R.id.categoryImage)
        nameTV = itemView.findViewById(R.id.categoryName)
    }

}