package com.dicoding.githubuser.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.response.DetailUserResponse
import com.dicoding.githubuser.database.FavoriteUser
import com.dicoding.githubuser.databinding.ActivityDetailUserBinding
import com.dicoding.githubuser.viewmodel.DetailUserViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    private lateinit var userData: DetailUserResponse
    private var favUser: List<FavoriteUser> = listOf()

    private val detailUserViewModel: DetailUserViewModel by viewModels<DetailUserViewModel>{
        DetailUserViewModel.Factory(this.application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username != null) {
            detailUserViewModel.fetchDetailUser(username)
            observeViewModel()

            sectionsPagerAdapter = SectionsPagerAdapter(this)
            sectionsPagerAdapter.username = username

            val viewPager: ViewPager2 = findViewById(R.id.view_pager)
            viewPager.adapter = sectionsPagerAdapter

            val tabs: TabLayout = findViewById(R.id.tabs)
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Followers"
                    else -> "Following"
                }
            }.attach()
        }

        binding.fabFavorite.setOnClickListener {
            if (::userData.isInitialized && favUser.isNotEmpty()) {
                detailUserViewModel.deleteFavorite(
                    FavoriteUser(
                        userData.login,
                        userData.avatarUrl,
                    )
                )
            } else if (::userData.isInitialized) {
                detailUserViewModel.insertFavorite(
                    FavoriteUser(
                        userData.login,
                        userData.avatarUrl,
                    )
                )
            }
        }
    }

    private fun observeViewModel() {
        detailUserViewModel.detailUser.observe(this) { detailUser ->
            userData = detailUser
            binding.tvUsername.text = detailUser.login
            binding.tvName.text = detailUser.name
            binding.tvId.text = detailUser.id.toString()

            val followersText = "${detailUser.followers} Follower"
            binding.tvFollowersCount.text = followersText

            val followingText = "${detailUser.following} Following"
            binding.tvFollowingCount.text = followingText

            Glide.with(this)
                .load(detailUser.avatarUrl)
                .placeholder(R.drawable.baseline_warning_24)
                .into(binding.ivUserAvatar)

            detailUserViewModel.getUsername(detailUser.login).observe(this) { favData ->
                favUser = favData

                if (favUser.isNotEmpty()) {
                    binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
                } else {
                    binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
                }
            }
        }

        detailUserViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) { binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }
}
