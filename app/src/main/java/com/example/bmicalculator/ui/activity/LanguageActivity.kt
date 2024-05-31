package com.example.bmicalculator.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bmicalculator.Constance
import com.example.bmicalculator.adapter.LanguageAdapter
import com.example.bmicalculator.base.BaseActivity
import com.example.bmicalculator.data.data
import com.example.bmicalculator.databinding.ActivityLanguageBinding
import com.example.bmicalculator.model.LanguageModel
import com.example.bmicalculator.utils.SharePreferencesUtils
import com.example.bmicalculator.utils.SystemUtils
import java.util.Arrays


class LanguageActivity : BaseActivity<ActivityLanguageBinding>(),LanguageAdapter.ILanguageItem {
    override fun setViewBinding(): ActivityLanguageBinding {
        return ActivityLanguageBinding.inflate(layoutInflater)
    }
    private var codeLang : String?=null
    private var codeLangDefault  : String?=null
    private val adapter by lazy {
        LanguageAdapter(this,data.languagelist, this)
    }

    private val check_language by lazy {
        SharePreferencesUtils.getBoolean(Constance.LANGUAGE,false)
    }

    override fun init() {
        initData()
        initView()
        initListener()
    }
    private fun initView(){
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter.setCheck(codeLangDefault)
        binding?.rcvLanguge?.layoutManager = layoutManager
        binding?.rcvLanguge?.adapter = adapter
    }

    private fun initData(){
        codeLangDefault = SystemUtils.getPreLanguage(baseContext)!!
        val langDefault = arrayOf("en", "es", "fr", "hi", "pt")
        if (!Arrays.asList(*langDefault).contains(codeLangDefault)) codeLang = "en"
        initListLanguage()
    }

    private fun initListener(){
        var isButtonClickHandled = false
        binding?.imgDone?.setOnClickListener(View.OnClickListener {
            if (!isButtonClickHandled) {
                isButtonClickHandled = true
                SystemUtils.setPreLanguage(this, codeLang)
                if (check_language!!) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, TutorialActivity::class.java)
                    startActivity(intent)
                }
                SharePreferencesUtils.putBoolean(Constance.LANGUAGE, true)
                finishAffinity()
                Handler(Looper.getMainLooper()).postDelayed({
                    isButtonClickHandled = false
                }, 500)
            }
        })
    }

    override fun onClickItemLanguage(code: String?, position: Int) {
        codeLang = code

    }

    fun Context.initListLanguage(): List<LanguageModel>{
        val codeLang = SystemUtils.getPreLanguage(this)
        val listLanguage = data.languagelist
        for (i in 0 until listLanguage.size){
            if (listLanguage[i].code == codeLang) {
                val selectedLanguage = listLanguage[i]
                listLanguage.removeAt(i)
                listLanguage.add(0, selectedLanguage)
                break
            }
        }
        return listLanguage
    }
}



