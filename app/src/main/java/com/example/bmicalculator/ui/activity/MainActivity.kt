package com.example.bmicalculator.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bmicalculator.Constance
import com.example.bmicalculator.R
import com.example.bmicalculator.base.BaseActivity
import com.example.bmicalculator.databinding.ActivityMainBinding
import com.example.bmicalculator.dialog.DialogRate
import com.example.bmicalculator.dialog.OnDialogDismissListener
import com.example.bmicalculator.ui.fragment.AboutFragment
import com.example.bmicalculator.ui.fragment.CalculatorFragment
import com.example.bmicalculator.ui.fragment.SettingFragment
import com.example.bmicalculator.ui.fragment.StatisticsFragment
import com.example.bmicalculator.utils.SharePreferencesUtils


class MainActivity : BaseActivity<ActivityMainBinding>(), OnDialogDismissListener {
    override fun setViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private var backPressTime: Long = 0
    private val dialogRate by lazy {
        DialogRate(this,this)
    }

    override fun init() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null){
            replaceFragment1(CalculatorFragment(), "CalculatorFragment")
        }

        binding!!.bottomNavBar.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.calculator -> {
                    replaceFragment1(CalculatorFragment(), "CalculatorFragment")
                }

                R.id.statistics -> {
                    replaceFragment1(StatisticsFragment())
                }

                R.id.setting -> {
                    replaceFragment1(SettingFragment(), "SettingFragment")
                }

                R.id.about -> {
                    replaceFragment1(AboutFragment(), "AboutFragment")
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        hideSystemUI()
        if (!SharePreferencesUtils.getBoolean(Constance.RATE,false)!! &&
            SharePreferencesUtils.getString(Constance.SELECT_RATE,null)!!.toInt() %2 == 1){
            if (backPressTime!! + 2000 > System.currentTimeMillis()){
                super.onBackPressed()
                return
            }else{
                dialogRate.show()
            }
            var isButtonClickHandled = false
            if (!isButtonClickHandled) {
                isButtonClickHandled = true
                backPressTime = System.currentTimeMillis()
                Handler(Looper.getMainLooper()).postDelayed({
                    isButtonClickHandled = false
                }, 500)
            }

        }else{
            super.onBackPressed()
            finishAffinity()
        }
    }
    fun hideSystemUI() {
        window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                )
    }

    fun replaceFragment1(fragment: Fragment, tag: String ?= null) {
        val transaction = supportFragmentManager.beginTransaction()


        val existingFragment = supportFragmentManager.findFragmentByTag(tag)

        if (existingFragment != null) {
            for (fragmentInStack in supportFragmentManager.fragments) {
                transaction.hide(fragmentInStack)
            }
            transaction.show(existingFragment)
        } else {
            transaction.add(R.id.frame_fragment, fragment, tag)
        }

        transaction.addToBackStack(tag)
        transaction.commit()
    }
    override fun onDialogDismiss() {
            finishAffinity()
        }
}