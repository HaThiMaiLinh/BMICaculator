package com.example.bmicalculator.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bmicalculator.R
import com.example.bmicalculator.base.BaseFragment
import com.example.bmicalculator.databinding.FragmentTeenagersBinding


class TeenagersFragment : BaseFragment<FragmentTeenagersBinding>() {


    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTeenagersBinding {
        return FragmentTeenagersBinding.inflate(layoutInflater)
    }
    override fun initView() {

    }
}