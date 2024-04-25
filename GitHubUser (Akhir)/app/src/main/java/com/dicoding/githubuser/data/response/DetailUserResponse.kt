package com.dicoding.githubuser.data.response

import com.google.gson.annotations.SerializedName

data class DetailUserResponse(

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("followers")
	val followers: Int,

	@field:SerializedName("following")
	val following: Int,

	@field:SerializedName("name")
	val name: String
)