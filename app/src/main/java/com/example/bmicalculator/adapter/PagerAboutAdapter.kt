package com.example.bmicalculator.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bmicalculator.ui.fragment.AdultsFragment
import com.example.bmicalculator.ui.fragment.TeenagersFragment

class PagerAboutAdapter(val fragment: Fragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AdultsFragment()
            1 -> TeenagersFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}