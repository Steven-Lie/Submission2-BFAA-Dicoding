package com.dicoding.githubusers

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubusers.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = mainBinding.searchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != "" && newText != null) {
                    mainViewModel.findUser(newText, this@MainActivity)
                } else {
                    mainViewModel.getUser()
                }
                return false
            }
        })

        mainViewModel.listUsers.observe(this, { users ->
            showUsers(users)
        })

        mainViewModel.isLoading.observe(this, { isLoading ->
            if (isLoading){
                mainBinding.progressBarSearch.visibility = View.VISIBLE
            } else {
                mainBinding.progressBarSearch.visibility = View.GONE
            }
        })
    }

    private fun showUsers(users: List<UsersData>){
        mainBinding.rvUsersSearch.layoutManager = LinearLayoutManager(this)
        val userAdapter = UserAdapter(users)
        mainBinding.rvUsersSearch.adapter = userAdapter
        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: UsersData) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: UsersData){
        val detailUserIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
        detailUserIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, user.login)
        startActivity(detailUserIntent)
    }
}