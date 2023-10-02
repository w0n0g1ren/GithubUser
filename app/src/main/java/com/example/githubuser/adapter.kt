package com.example.githubuser

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.databinding.IvUserBinding

class adapter(private val context: Context) : ListAdapter<ItemsItem, adapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: IvUserBinding,private val context: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem){
            binding.userTv.text = user.login
            Glide.with(itemView)
                .load(user.avatarUrl)
                .into(binding.userPic)

            binding.ivLayout.setOnClickListener {
                val intent = Intent(context,DetailActivity::class.java)
                intent.putExtra("nama",user.login)
                intent.putExtra("avatar",user.avatarUrl)
                context.startActivity(intent)
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = IvUserBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val context = context
        return MyViewHolder(binding,context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bind = getItem(position)
        holder.bind(bind)
    }
}