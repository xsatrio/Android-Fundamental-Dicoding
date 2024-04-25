package com.dicoding.githubuser.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUserViewModel: DetailUserViewModel
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username != null) {
            detailUserViewModel = ViewModelProvider(this)[DetailUserViewModel::class.java]
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
    }

    private fun observeViewModel() {
        detailUserViewModel.detailUser.observe(this) { detailUser ->
            binding.tvUsername.text = detailUser.login
            binding.tvName.text = detailUser.name

            val followersText = "${detailUser.followers} Follower"
            binding.tvFollowersCount.text = followersText

            val followingText = "${detailUser.following} Following"
            binding.tvFollowingCount.text = followingText

            Glide.with(this)
                .load(detailUser.avatarUrl)
                .placeholder(R.drawable.baseline_warning_24)
                .into(binding.ivUserAvatar)
        }

        detailUserViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}
