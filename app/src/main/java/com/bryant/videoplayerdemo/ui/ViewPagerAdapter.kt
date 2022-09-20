package com.bryant.videoplayerdemo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bryant.videoplayerdemo.data.P

class ViewPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    var dataList: List<P?> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = dataList.size

    override fun createFragment(position: Int): Fragment {
        val fragment = PageFragment()
        fragment.arguments = Bundle().apply {
            putInt(ARG_INDEX, position)
        }
        return fragment
    }
}