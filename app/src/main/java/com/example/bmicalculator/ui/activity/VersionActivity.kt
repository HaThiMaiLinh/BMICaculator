package com.example.bmicalculator.ui.activity

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import com.example.bmicalculator.R
import com.example.bmicalculator.base.BaseActivity
import com.example.bmicalculator.databinding.ActivityVersionBinding


class VersionActivity : BaseActivity<ActivityVersionBinding>() {
    override fun setViewBinding(): ActivityVersionBinding {
        return ActivityVersionBinding.inflate(layoutInflater)
    }

    @SuppressLint("SetTextI18n", "NewApi")
    override fun init() {
        val pInfo: PackageInfo =
            this.getPackageManager().getPackageInfo(this.getPackageName(), 0)
        val version = pInfo.versionName
            binding!!.tvDesVersion.text = getString(R.string.tv_version) + " " + version
        binding!!.imgBack.setOnClickListener {
            finish()
        }
    }

}