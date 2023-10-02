package com.example.githubuser

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.database.User
import com.example.githubuser.repository.Repository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mUserRepository: Repository = Repository(application)

    fun getAllNotes(): LiveData<List<User>> = mUserRepository.getAllNotes()
}