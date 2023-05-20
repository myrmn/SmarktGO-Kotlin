package com.esprit.smarktgo.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.esprit.smarktgo.R
import com.esprit.smarktgo.model.Item
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.view.CartGroupActivity


class UserAdapter(val mActivity: CartGroupActivity) : RecyclerView.Adapter<UserViewHolder>() {

    private var list = ArrayList<User>()


    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<User>) {
        this.list = list as ArrayList<User>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.nameTV.text = list[position].fullName
        if(mActivity.cartGroupViewModel.groupMembers.contains(list[position]))
            holder.addUserCardView.isVisible = false
        else
        holder.addUserCardView.setOnClickListener {
            mActivity.cartGroupViewModel.addUser(list[position].id)
            holder.addUserCardView.isVisible = false
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nameTV : TextView
    val addUserCardView : CardView

    init {
        nameTV = itemView.findViewById(R.id.userName)
        addUserCardView = itemView.findViewById(R.id.addUserCardView)
    }

}