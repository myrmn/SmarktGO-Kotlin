package com.esprit.smarktgo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.esprit.smarktgo.R
import com.esprit.smarktgo.model.HomeCategory
import com.esprit.smarktgo.model.Review
import com.esprit.smarktgo.model.Supermarket
import com.esprit.smarktgo.utils.RetrofitInstance.BASE_URL
import com.esprit.smarktgo.view.SupermarketActivity

class ReviewAdapter(val mActivity: SupermarketActivity) : RecyclerView.Adapter<ReviewViewHolder>() {

    private var list = ArrayList<Review>()

    fun setList(list: List<Review>) {
        this.list = list as ArrayList<Review>
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rating_item, parent, false)
        return ReviewViewHolder(view)
    }
    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.title.text = list[position].title
        holder.username.text = list[position].username
        holder.description.text = list[position].description
        holder.rating.rating=list[position].rating
        val date=list[position].date.subSequence(0,10)
        val time=list[position].date.subSequence(11,16)
        holder.date.text="$date"+"  "+"$time"




    }

    override fun getItemCount(): Int {
        return list.size
    }

}

class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title : TextView
    val description : TextView
    val username:TextView
    val date:TextView
    val rating:RatingBar
    init {
        title = itemView.findViewById(R.id.reviewTitle)
        description = itemView.findViewById(R.id.reviewDescription)
        date=itemView.findViewById(R.id.reviewDate)
        rating=itemView.findViewById(R.id.ratingBar)
        username=itemView.findViewById(R.id.reviewUsername)
        rating.setIsIndicator(true)
    }

}