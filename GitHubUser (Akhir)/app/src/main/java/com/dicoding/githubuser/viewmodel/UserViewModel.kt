package com.dicoding.githubuser.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.githubuser.SettingPreferences
import com.dicoding.githubuser.data.response.GithubResponse
import com.dicoding.githubuser.data.response.ItemsItem
import com.dicoding.githubuser.data.retrofit.ApiConfig
import com.dicoding.githubuser.dataStore
import com.dicoding.githubuser.repository.FavoriteUserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(private val pref: SettingPreferences, private val application: Application) : AndroidViewModel(application){

    private val _userList = MutableLiveData<List<ItemsItem>>()
    val userList: LiveData<List<ItemsItem>> = _userList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val favUserRepo: FavoriteUserRepository = FavoriteUserRepository(application)

    init {
        fetchDataFromApi("Arif")
    }

    fun getFavoriteUser() = favUserRepo.getFavoriteUser()

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

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        private val pref: SettingPreferences = SettingPreferences.getInstance(application.dataStore)
        @Suppress("UNCHECKED_CAST")

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            UserViewModel(pref ,application) as T
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