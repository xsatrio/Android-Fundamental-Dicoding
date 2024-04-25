package com.dicoding.githubuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupRecyclerView()

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        observeViewModel()

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchView.hide()
                    val username = searchView.text.toString()
                    fetchData(username)
                    true
                }
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu1 -> {
                    val username = "xsatrio"
                    val intent = Intent(this, DetailUserActivity::class.java)
                    intent.putExtra(DetailUserActivity.EXTRA_USERNAME, username)

                    startActivity(intent)
                    true
                } else -> false
            }
        }
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
        userViewModel.userList.observe(this) { userList ->
            adapter.submitList(userList)
        }

        userViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        userViewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                showToast(it)
            }
        }
    }

    private fun fetchData(username: String) {
        userViewModel.fetchDataFromApi(username)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToDetail(username: String) {
        val intent = Intent(this, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.EXTRA_USERNAME, username)
        startActivity(intent)
    }
}