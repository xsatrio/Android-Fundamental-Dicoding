package com.dicoding.githubuser.data.retrofit

import com.dicoding.githubuser.BuildConfig
import com.dicoding.githubuser.data.response.DetailUserResponse
import com.dicoding.githubuser.data.response.GithubResponse
import com.dicoding.githubuser.data.response.ItemsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization: token ${BuildConfig.GITHUB_API_KEY}"
    }

    @GET("search/users")
    @Headers(AUTHORIZATION_HEADER)
    fun searchUsers(
        @Query("q") query: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    @Headers(AUTHORIZATION_HEADER)
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/{type}")
    @Headers(AUTHORIZATION_HEADER)
    fun getUserFollow(@Path("username") username: String, @Path("type") type: String): Call<List<ItemsItem>>

}
