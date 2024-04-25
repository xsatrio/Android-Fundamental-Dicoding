package com.dicoding.githubuser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.githubuser.database.FavoriteUser
import com.dicoding.githubuser.database.FavoriteUserDao
import com.dicoding.githubuser.database.FavoriteUserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {

    private val favoriteUserDao : FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserDatabase.getInstance(application)
        favoriteUserDao = db.favoriteUserDao()
    }
    fun getFavoriteUser(): LiveData<List<FavoriteUser>> = favoriteUserDao.getFavoriteUser()

    fun insert(user: FavoriteUser) {
        executorService.execute { favoriteUserDao.addFavoriteUser(user) }
    }

    fun delete(user: FavoriteUser) {
        executorService.execute { favoriteUserDao.removeFavoriteUser(user)}
    }

    fun getUsername(username: String): LiveData<List<FavoriteUser>> = favoriteUserDao.getUsername(username)
}
