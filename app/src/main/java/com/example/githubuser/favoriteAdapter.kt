package com.example.githubuser

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.database.User
import com.example.githubuser.databinding.IvUserBinding
import com.example.githubuser.helper.UserDifCallback

class FavoriteAdapter(private val context: Context) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private val listFavorite = ArrayList<User>()

    fun setListUser(listNotes: List<User>) {
        val diffCallback = UserDifCallback(this.listFavorite, listNotes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavorite.clear()
        this.listFavorite.addAll(listNotes)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class FavoriteViewHolder(private val binding: IvUserBinding,private val context: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                userTv.text = user.username
                Glide.with(itemView)
                    .load(user.avatar)
                    .into(binding.userPic)

                binding.ivLayout.setOnClickListener {
                    val intent = Intent(context,DetailActivity::class.java)
                    intent.putExtra("nama",user.username)
                    intent.putExtra("avatar",user.avatar)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = IvUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val context = context
        return FavoriteViewHolder(binding,context)
    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

}