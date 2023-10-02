package com.example.githubuser.tema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TemaViewModelFactory(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TemaViewModel::class.java)) {
            return TemaViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}