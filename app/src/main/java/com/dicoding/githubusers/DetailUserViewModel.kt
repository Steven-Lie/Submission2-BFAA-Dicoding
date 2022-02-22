package com.dicoding.githubusers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel : ViewModel(){
    private val _userDetail = MutableLiveData<UserDetailResponse>()
    val userDetail: LiveData<UserDetailResponse> = _userDetail

    private val _followers = MutableLiveData<List<UsersData>>()
    val followers: LiveData<List<UsersData>> = _followers

    private val _following = MutableLiveData<List<UsersData>>()
    val following: LiveData<List<UsersData>> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUserDetail(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<UserDetailResponse>{
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoading.value= false
                if (response.isSuccessful){
                    _userDetail.value = response.body()
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getFollowersList(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<UsersData>>{
            override fun onResponse(
                call: Call<List<UsersData>>,
                response: Response<List<UsersData>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _followers.value = response.body()
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

    fun getFollowingList(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<UsersData>>{
            override fun onResponse(
                call: Call<List<UsersData>>,
                response: Response<List<UsersData>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _following.value = response.body()
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
        private const val TAG = "DetailUserViewModel"
    }
}