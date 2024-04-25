package com.dicoding.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.databinding.ActivityFavoriteBinding
import com.dicoding.githubuser.viewmodel.UserViewModel
import com.dicoding.githubuser.data.response.ItemsItem

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UserAdapter
    private val userViewModel: UserViewModel by viewModels<UserViewModel>{
        UserViewModel.Factory(this.application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupRecyclerView()

        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter { selectedUsername ->
            navigateToDetail(selectedUsername)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))

        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        userViewModel.getFavoriteUser().observe(this) { userList ->
            val itemList: List<ItemsItem> = userList.map { data ->
                ItemsItem(
                    data.username,
                    data.avatarUrl.toString()
                )
            }
            adapter.submitList(itemList)
        }

        userViewModel.isLoading.observe(this) { isLoading ->
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

    private fun navigateToDetail(username: String) {
        val intent = Intent(this, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.EXTRA_USERNAME, username)
        startActivity(intent)
    }
}