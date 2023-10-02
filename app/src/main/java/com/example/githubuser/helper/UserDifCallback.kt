package com.example.githubuser.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.githubuser.database.User

class UserDifCallback(private val oldUserList: List<User>, private val newUserList: List<User>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldUserList.size
    override fun getNewListSize(): Int = newUserList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldUserList[oldItemPosition].username == newUserList[newItemPosition].username
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNote = oldUserList[oldItemPosition]
        val newNote = newUserList[newItemPosition]
        return oldNote.username == newNote.username && oldNote.avatar == newNote.username
    }
}