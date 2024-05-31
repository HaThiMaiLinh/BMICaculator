package com.example.bmicalculator.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bmicalculator.ui.fragment.DailyFragment
import com.example.bmicalculator.ui.fragment.MonthlyFragment
import com.example.bmicalculator.ui.fragment.WeeklyFragment

class PagerStatisticsAdapter(val fragment: Fragment):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DailyFragment()
            1 -> WeeklyFragment()
            2-> MonthlyFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}