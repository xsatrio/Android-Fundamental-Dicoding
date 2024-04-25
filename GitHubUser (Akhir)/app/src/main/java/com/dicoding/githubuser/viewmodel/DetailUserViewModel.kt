package com.dicoding.githubuser.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubuser.data.response.DetailUserResponse
import com.dicoding.githubuser.data.retrofit.ApiConfig
import com.dicoding.githubuser.database.FavoriteUser
import com.dicoding.githubuser.repository.FavoriteUserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

    class DetailUserViewModel(application: Application) : AndroidViewModel(application) {

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()

    private val favUserRepo: FavoriteUserRepository = FavoriteUserRepository(application)

    fun insertFavorite(note: FavoriteUser) = favUserRepo.insert(note)

    fun deleteFavorite(note: FavoriteUser) = favUserRepo.delete(note)

    fun getUsername(username: String) = favUserRepo.getUsername(username)

    fun fetchDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    val statusCode = response.code()
                    handleHttpError(statusCode, response.message())
                    Log.e("DetailUserViewModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message ?: "Unknown error"
                Log.e("DetailUserViewModel", "onFailure: ${t.message}")
            }
        })
    }

    private fun handleHttpError(statusCode: Int, errorMessage: String) {
        when (statusCode) {
            401 -> _errorMessage.value = "$statusCode : Bad Request"
            403 -> _errorMessage.value = "$statusCode : Forbidden"
            404 -> _errorMessage.value = "$statusCode : Not Found"
            else -> _errorMessage.value = "$statusCode : $errorMessage"
        }
    }
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailUserViewModel(application) as T
    }
}
