package com.example.bmicalculator.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import com.example.bmicalculator.R
import com.example.bmicalculator.ViewModel.BmiViewModel
import com.example.bmicalculator.ViewModel.BmiViewModelFactory
import com.example.bmicalculator.adapter.PagerAboutAdapter
import com.example.bmicalculator.base.Application
import com.example.bmicalculator.base.BaseFragment
import com.example.bmicalculator.databinding.FragmentAboutBinding
import com.google.android.material.tabs.TabLayoutMediator


class AboutFragment : BaseFragment<FragmentAboutBinding>() {

    private val adapter by lazy {
        PagerAboutAdapter(this)
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentAboutBinding {
        return FragmentAboutBinding.inflate(layoutInflater)
    }

    override fun initView() {
        binding.viewpagerAbout.adapter = adapter
        val mediator = TabLayoutMediator(
            binding.idTablayoutAbout, binding.viewpagerAbout
        ) { tab, position ->
            when (position) {
                0 -> tab.text = requireActivity().getString(R.string.tab_adults)
                1 -> tab.text = requireActivity().getString(R.string.tab_teenagers)
            }
        }
        mediator.attach()
    }

}