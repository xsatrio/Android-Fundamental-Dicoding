package com.dicoding.githubuser.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.response.GithubResponse
import com.dicoding.githubuser.data.response.ItemsItem
import com.dicoding.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {

    private val _userList = MutableLiveData<List<ItemsItem>>()
    val userList: LiveData<List<ItemsItem>> = _userList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        fetchDataFromApi("Arif")
    }

    fun fetchDataFromApi(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUsers(username)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userList.value = responseBody.items
                    }
                } else {
                    val statusCode = response.code()
                    handleHttpError(statusCode, response.message())
                    Log.e("UserViewModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message ?: "Unknown error"
                Log.e("UserViewModel", "onFailure: ${t.message}")
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
}