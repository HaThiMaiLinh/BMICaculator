package com.example.bmicalculator.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bmicalculator.R
import com.example.bmicalculator.base.BaseFragment
import com.example.bmicalculator.databinding.FragmentAdultsBinding


class AdultsFragment : BaseFragment<FragmentAdultsBinding>() {
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdultsBinding {
        return FragmentAdultsBinding.inflate(layoutInflater)
    }

    override fun initView() {

    }

}