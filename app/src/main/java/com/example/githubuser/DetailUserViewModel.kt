package com.example.githubuser

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.database.User
import com.example.githubuser.repository.Repository

class DetailUserViewModel(application: Application) : ViewModel() {
    private val mNoteRepository: Repository = Repository(application)
    fun insert(user: User) {
        mNoteRepository.insert(user)
    }
    fun searchuser(username: String): LiveData<User> = mNoteRepository.searchUser(username)

    fun delete(user: User){
        mNoteRepository.delete(user)
    }
}