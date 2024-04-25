package com.dicoding.githubuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter

    private var darkMode: Boolean = false
    private val userViewModel: UserViewModel by viewModels<UserViewModel>{
        UserViewModel.Factory(this@MainActivity.application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupRecyclerView()

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
                }
                R.id.menu2 -> {
                    val intent = Intent(this, FavoriteActivity::class.java)

                    startActivity(intent)
                    true
                }R.id.menu3 -> {
                    if (!darkMode) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        userViewModel.saveThemeSetting(true)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        userViewModel.saveThemeSetting(false)
                    }
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

        userViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            darkMode = isDarkModeActive
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.topAppBar.menu.findItem(R.id.menu3).setIcon(R.drawable.ic_light)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.topAppBar.menu.findItem(R.id.menu3).setIcon(R.drawable.ic_dark)
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