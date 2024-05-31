package com.example.bmicalculator.ui.activity


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.bmicalculator.Constance
import com.example.bmicalculator.base.BaseActivity
import com.example.bmicalculator.databinding.ActivitySplashBinding
import com.example.bmicalculator.utils.SharePreferencesUtils


class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private var checkLogApp : Int = 0
    private val handler = Handler()
    override fun setViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun init() {
        initView()
    }
    fun initView(){

        checkLogApp = SharePreferencesUtils.getString(Constance.SELECT_RATE,0.toString())!!.toInt()
        checkLogApp++;
        SharePreferencesUtils.putString(Constance.SELECT_RATE,checkLogApp.toString())
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }

    override fun onStart() {
        super.onStart()
        handler.postDelayed({
            if (SharePreferencesUtils.getBoolean(Constance.LANGUAGE,false)!! && SharePreferencesUtils.getBoolean(Constance.FIRST_LOG_APP,false)!!){
                intent = Intent(this, MainActivity::class.java)
            }else if (SharePreferencesUtils.getBoolean(Constance.LANGUAGE,false)!! && !SharePreferencesUtils.getBoolean(Constance.FIRST_LOG_APP,false)!!
                && !SharePreferencesUtils.getBoolean(Constance.PERMISSION,false)!!){
                intent = Intent(this, TutorialActivity::class.java)
            }else if (SharePreferencesUtils.getBoolean(Constance.LANGUAGE,false)!! && !SharePreferencesUtils.getBoolean(Constance.FIRST_LOG_APP,false)!!
                && SharePreferencesUtils.getBoolean(Constance.PERMISSION,false)!!){
                intent = Intent(this, PermissionActivity::class.java)
            }else{
                intent = Intent(this, LanguageActivity::class.java)
            }
            intent!!.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }, 2000)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null)
    }

}