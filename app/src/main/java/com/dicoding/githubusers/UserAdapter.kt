package com.dicoding.githubusers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubusers.databinding.ItemUsersBinding

class UserAdapter(private val listUser: List<UsersData>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(var itemUsersBinding: ItemUsersBinding) : RecyclerView.ViewHolder(itemUsersBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(listUser[position].avatarUrl)
            .circleCrop()
            .into(holder.itemUsersBinding.imgProfile)
        holder.itemUsersBinding.tvUsername.text = listUser[position].login

        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(listUser[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback{
        fun onItemClicked(data: UsersData)
    }
}