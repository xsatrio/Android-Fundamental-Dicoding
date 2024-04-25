package com.dicoding.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteUserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addFavoriteUser(user: FavoriteUser)

    @Update
    fun updateFavoriteUser(user: FavoriteUser)

    @Delete
    fun removeFavoriteUser(user: FavoriteUser)

    @Query("SELECT * FROM favoriteuser")
    fun getFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT * from favoriteuser WHERE username = :username")
    fun getUsername(username: String): LiveData<List<FavoriteUser>>
}

