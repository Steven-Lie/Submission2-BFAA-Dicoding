package com.dicoding.githubusers

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _listUser = MutableLiveData<List<UsersData>>()
    val listUsers : LiveData<List<UsersData>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    init {
        getUser()
    }

    fun findUser(username: String, context: Context){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserSearched(username)
        client.enqueue(object : Callback<UserSearchResponse>{
            override fun onResponse(
                call: Call<UserSearchResponse>,
                response: Response<UserSearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _listUser.value = response.body()?.UsersData
                } else {
                    Toast.makeText(context, "Request Limit exceed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getUser(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers()
        client.enqueue(object : Callback<List<UsersData>>{
            override fun onResponse(
                call: Call<List<UsersData>>,
                response: Response<List<UsersData>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _listUser.value = response.body()
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UsersData>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object{
        private const val TAG = "MainViewModel"
    }
}