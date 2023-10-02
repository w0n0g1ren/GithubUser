package com.example.githubuser

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerAdapter(activity : AppCompatActivity,username : String) : FragmentStateAdapter(activity) {
    var user : String? = username
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment? = null
        when (position) {
            0 ->fragment = FollowersFragment(user)
            1 ->fragment = FollowingFragment(user)
        }
        return fragment as Fragment
    }
}