package com.example.bmicalculator.ui.activity

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bmicalculator.Constance
import com.example.bmicalculator.ViewModel.BmiViewModel
import com.example.bmicalculator.ViewModel.BmiViewModelFactory
import com.example.bmicalculator.adapter.RecentAdapter
import com.example.bmicalculator.base.Application
import com.example.bmicalculator.base.BaseActivity
import com.example.bmicalculator.databinding.ActivityRecentBinding
import com.example.bmicalculator.model.dto.Bmi
import com.example.bmicalculator.utils.SharePreferencesUtils

class RecentActivity : BaseActivity<ActivityRecentBinding>() {

    lateinit var adapter: RecentAdapter
    private val isSave by lazy {
        SharePreferencesUtils.getBoolean(Constance.SAVE,false)
    }
    private val viewModel: BmiViewModel by viewModels {
        BmiViewModelFactory((application as Application).repository)
    }
    override fun setViewBinding(): ActivityRecentBinding {
        return ActivityRecentBinding.inflate(layoutInflater)
    }
    override fun init() {
        initAdapter()
        initView()
        initListerner()
    }
    private fun initView(){
        viewModel.allBmi.observe( this){listbmi->
            if (listbmi.size == 0){
                binding!!.rcvRecent.visibility = View.GONE
            }
        }
    }

    private fun initAdapter(){
        val intent = Intent(this,BMIActivity::class.java)
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = RecentAdapter(this,object : RecentAdapter.OnItemRecentClickListener{
            override fun onClickItemListener(item: Bmi?) {
                SharePreferencesUtils.putBoolean(Constance.ISRECENT,true)
                intent.putExtra(Constance.BMI,item)
                startActivity(intent)
            }
        })

        viewModel.allBmi.observe( this){listbmi->
            listbmi.let { adapter.setItems(listbmi) }
        }
        binding!!.rcvRecent.layoutManager = layoutManager
        binding!!.rcvRecent.adapter = adapter
    }
    private fun initListerner(){
        var isClickBack = false
        binding!!.imgBack.setOnClickListener {
            if (!isClickBack){
                isClickBack =true
                finish()
                Handler(Looper.getMainLooper()).postDelayed({
                    isClickBack = false
                },500)
            }
        }
    }

}