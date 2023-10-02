package com.example.githubuser.database

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class User (
    @NonNull
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "username")
    var username : String,

    @ColumnInfo(name = "avatar")
    var avatar : String? = null
) : Parcelable
