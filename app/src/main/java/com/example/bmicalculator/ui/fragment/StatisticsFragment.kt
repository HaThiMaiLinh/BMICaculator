package com.example.bmicalculator.ui.fragment

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.bmicalculator.R
import com.example.bmicalculator.adapter.PagerStatisticsAdapter
import com.example.bmicalculator.base.BaseFragment
import com.example.bmicalculator.databinding.FragmentStatisticsBinding
import com.example.bmicalculator.ui.activity.MainActivity
import com.google.android.material.tabs.TabLayoutMediator


class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>() {


    private val adapter by lazy {
        PagerStatisticsAdapter(this)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStatisticsBinding {
        return FragmentStatisticsBinding.inflate(layoutInflater)
    }

    override fun initView() {
        binding.viewpagerChart.adapter = adapter
        binding.viewpagerChart.isUserInputEnabled = false
        binding.tvUpdate.isSelected = true
        val mediator = TabLayoutMediator(
            binding.idTablayoutStatistics, binding.viewpagerChart
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = requireActivity().getString(R.string.tab_daily)
                }
                1 -> {
                    tab.text = requireActivity().getString(R.string.tab_weekly)
                }
                2-> {
                    tab.text = requireActivity().getString(R.string.tab_monthly)
                }
            }
        }
        mediator.attach()
        initListener()
    }
    private fun initListener(){
        var isClickUpdate = false
        binding.tvUpdate.setOnClickListener {
            if (!isClickUpdate){
                isClickUpdate = true
                startActivity(Intent(activity,MainActivity::class.java))
                activity?.finishAffinity()
                Handler(Looper.getMainLooper()).postDelayed({
                    isClickUpdate = false
                },500)
            }
        }
    }
}