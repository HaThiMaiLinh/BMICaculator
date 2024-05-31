package com.example.bmicalculator.ui.activity

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.bmicalculator.Constance
import com.example.bmicalculator.adapter.TutorialAdapter
import com.example.bmicalculator.base.BaseActivity
import com.example.bmicalculator.data.data
import com.example.bmicalculator.databinding.ActivityTutorialBinding
import com.example.bmicalculator.utils.SharePreferencesUtils
import com.google.android.material.tabs.TabLayoutMediator


class TutorialActivity : BaseActivity<ActivityTutorialBinding>() {

    private val adapter by lazy {
        TutorialAdapter(this,data.tutoriallist)
    }

    override fun setViewBinding(): ActivityTutorialBinding {
        return ActivityTutorialBinding.inflate(layoutInflater)
    }

    override fun init() {
        initView()
        initListener()
    }

    private fun initView(){
        binding!!.viewpagerTutorial.adapter = adapter
        binding!!.wormDotsIndicator.attachTo(binding!!.viewpagerTutorial)
    }

    private fun initListener(){
        var isButtonClickHandled = false
        binding?.tvNext?.setOnClickListener(View.OnClickListener {

            if (!isButtonClickHandled) {
                isButtonClickHandled = true
               SharePreferencesUtils.putString(Constance.TUTORIAL_PAGE,binding?.viewpagerTutorial?.currentItem.toString())
                if (binding?.viewpagerTutorial?.currentItem == 2) {
                    val intent = Intent(this, PermissionActivity::class.java)
                    startActivity(intent)
                    finish()
                    Handler(Looper.getMainLooper()).postDelayed({
                        isButtonClickHandled = false
                    }, 500)
                } else {
                    binding?.viewpagerTutorial?.setCurrentItem(binding!!.viewpagerTutorial.currentItem + 1, true)
                    Handler(Looper.getMainLooper()).postDelayed({
                        isButtonClickHandled = false
                    }, 500)
                }
            }
        })
    }
}