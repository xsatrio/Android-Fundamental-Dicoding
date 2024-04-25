package com.dicoding.githubuser.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.response.ItemsItem
import com.dicoding.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {

    private val _followers = MutableLiveData<List<ItemsItem>>()
    val followers: LiveData<List<ItemsItem>> = _followers

    private val _following = MutableLiveData<List<ItemsItem>>()
    val following: LiveData<List<ItemsItem>> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchFollowers(username: String) {
        fetchData(username, "followers")
    }

    fun fetchFollowing(username: String) {
        fetchData(username, "following")
    }

    private fun fetchData(username: String, type: String) {
        if (_isLoading.value != true) {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getUserFollow(username, type)
            client.enqueue(object : Callback<List<ItemsItem>> {
                override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            if (type == "followers") {
                                _followers.value = responseBody
                            } else {
                                _following.value = responseBody
                            }
                        }
                    } else {
                        val statusCode = response.code()
                        handleHttpError(statusCode, response.message())
                        Log.e("FollowViewModel", "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    _isLoading.value = false
                    _errorMessage.value = t.message ?: "Unknown error"
                    Log.e("FollowViewModel", "onFailure: ${t.message}")
                }
            })
        }
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
