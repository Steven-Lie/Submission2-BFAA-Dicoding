package com.dicoding.githubusers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubusers.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    private lateinit var detailUserBinding: ActivityDetailUserBinding
    private val detailUserViewModel by viewModels<DetailUserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailUserBinding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(detailUserBinding.root)
        val username = intent.getStringExtra(EXTRA_USERNAME).toString()

        val sectionsPagerAdapter = SectionsPagerAdapter(this, username)
        val viewPager: ViewPager2 = detailUserBinding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = detailUserBinding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailUserViewModel.getUserDetail(username)

        detailUserViewModel.userDetail.observe(this, { userDetail ->
            showDetailUser(userDetail)
        })

        detailUserViewModel.isLoading.observe(this, { isLoading ->
            if (isLoading){
                detailUserBinding.progressBarDetail.visibility = View.VISIBLE
            } else {
                detailUserBinding.progressBarDetail.visibility = View.GONE
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun showDetailUser(detailUser: UserDetailResponse){
        Glide.with(this.applicationContext)
            .load(detailUser.avatarUrl)
            .circleCrop()
            .into(detailUserBinding.imgProfileDetail)
        detailUserBinding.tvNameDetail.text = detailUser.name
        detailUserBinding.tvUsernameDetail.text = detailUser.login
        detailUserBinding.includeFollowersFollowing.imgFollowersFollowingDetail.setImageResource(R.drawable.ic_baseline_people_alt_24)
        detailUserBinding.includeFollowersFollowing.tvFollowersDetail.text = getString(R.string.amount_followers, detailUser.followers.toString())
        detailUserBinding.includeFollowersFollowing.tvFollowingDetail.text = getString(R.string.amount_following, detailUser.following.toString())
        detailUserBinding.includeRepository.imgRepositoryDetail.setImageResource(R.drawable.ic_baseline_book_24)
        detailUserBinding.includeRepository.tvRepositoryDetail.text = getString(R.string.amount_repository, detailUser.publicRepos.toString())
        if (detailUser.company == null){
            detailUserBinding.includeCompany.imgCompanyDetail.visibility = View.GONE
            detailUserBinding.includeCompany.tvCompanyDetail.visibility = View.GONE
        } else {
            detailUserBinding.includeCompany.imgCompanyDetail.setImageResource(R.drawable.ic_baseline_apartment_24)
            detailUserBinding.includeCompany.tvCompanyDetail.text = detailUser.company
        }
        if (detailUser.location == null){
            detailUserBinding.includeLocation.imgLocationDetail.visibility = View.GONE
            detailUserBinding.includeLocation.tvLocationDetail.visibility = View.GONE
        } else {
            detailUserBinding.includeLocation.imgLocationDetail.setImageResource(R.drawable.ic_baseline_location_white)
            detailUserBinding.includeLocation.tvLocationDetail.text = detailUser.location
        }
    }

    companion object{
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
        const val EXTRA_USERNAME = "extra_username"
    }
}