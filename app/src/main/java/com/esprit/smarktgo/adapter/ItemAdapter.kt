package com.esprit.smarktgo.adapter

import android.annotation.SuppressLint
import android.app.PendingIntent.getActivity
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.esprit.smarktgo.R
import com.esprit.smarktgo.model.Item
import com.esprit.smarktgo.view.CartFragment
import com.esprit.smarktgo.view.ItemsActivity

class ItemAdapter(val itemsActivity: ItemsActivity?, val cartFragment: CartFragment?) : RecyclerView.Adapter<ItemViewHolder>() {

    private var list = ArrayList<Item>()


    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Item>) {
        this.list = list as ArrayList<Item>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_item, parent, false)
        return ItemViewHolder(view)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(list[position].image)
            .override(165,165)
            .into(holder.imageV)
        holder.nameTV.text = list[position].name
        holder.descriptionTV.text = list[position].description
        holder.priceTV.text = "${list[position].price} TND"
        holder.iconCardView.setOnClickListener {
                if(itemsActivity!=null)
                {
                    itemsActivity.orderDialog.item = list[position]
                    itemsActivity.orderDialog.show()
                }
                else
                    cartFragment!!.cartViewModel.removeItem(position)
        }

        if(cartFragment!=null)
        {
            holder.iconCardView.setBackgroundColor(Color.RED)
            holder.icon.setImageDrawable(ContextCompat.getDrawable(cartFragment.requireContext(),R.drawable.ic_baseline_minus_24))
            holder.quantityTV.isVisible = true
            holder.quantityTV.text = "x${list[position].quantity}"
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nameTV : TextView
    val priceTV : TextView
    val descriptionTV : TextView
    val imageV : ImageView
    val iconCardView : CardView
    val quantityTV : TextView
    val icon : ImageView


    init {
        imageV = itemView.findViewById(R.id.itemImage)
        nameTV = itemView.findViewById(R.id.itemName)
        priceTV = itemView.findViewById(R.id.itemPrice)
        descriptionTV = itemView.findViewById(R.id.itemDescription)
        iconCardView = itemView.findViewById(R.id.iconCardView)
        quantityTV = itemView.findViewById(R.id.itemQuantity)
        icon = itemView.findViewById(R.id.addIcon)
    }

}