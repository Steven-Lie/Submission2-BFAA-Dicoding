package com.dicoding.githubusers

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FollowersFragment : Fragment() {
    private val viewModel by activityViewModels<DetailUserViewModel>()
    private lateinit var rvUsers: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvUsers = view.findViewById(R.id.rv_followers_following)
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(USERNAME)

        if (index == 0){
            viewModel.getFollowersList(username.toString())
            viewModel.followers.observe(viewLifecycleOwner, { users->
                showFollow(users)
            })
        } else {
            viewModel.getFollowingList(username.toString())
            viewModel.following.observe(viewLifecycleOwner, { users->
                showFollow(users)
            })
        }
    }

    private fun showFollow(users: List<UsersData>){
        rvUsers.layoutManager = LinearLayoutManager(activity)
        val userAdapter = UserAdapter(users)
        rvUsers.adapter = userAdapter
        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: UsersData) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: UsersData){
        val detailUserIntent = Intent(activity, DetailUserActivity::class.java)
        detailUserIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, user.login)
        startActivity(detailUserIntent)
    }

    companion object{
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val USERNAME = "username"

        @JvmStatic
        fun newInstance(index: Int, username: String) =
            FollowersFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                    putString(USERNAME, username)
                }
            }
    }
}