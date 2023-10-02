package com.example.githubuser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuser.database.User
import com.example.githubuser.database.UserDao
import com.example.githubuser.database.UserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Repository(application : Application) {
    private val mUserDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserRoomDatabase.getDatabase(application)
        mUserDao = db.noteDao()
    }

    fun getAllNotes(): LiveData<List<User>> = mUserDao.getAllNotes()

    fun searchUser(username : String): LiveData<User> = mUserDao.getFavoriteUserByUsername(username)
    fun insert(note: User) {
        executorService.execute { mUserDao.insert(note) }
    }
    fun delete(note: User) {
        executorService.execute { mUserDao.delete(note) }
    }
    fun update(note: User) {
        executorService.execute { mUserDao.update(note) }
    }
}