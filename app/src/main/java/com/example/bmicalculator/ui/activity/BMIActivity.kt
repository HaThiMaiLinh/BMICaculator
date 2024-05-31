package com.example.bmicalculator.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.example.bmicalculator.Constance
import com.example.bmicalculator.R
import com.example.bmicalculator.ViewModel.BmiViewModel
import com.example.bmicalculator.ViewModel.BmiViewModelFactory
import com.example.bmicalculator.base.Application
import com.example.bmicalculator.base.BaseActivity
import com.example.bmicalculator.databinding.ActivityBmiactivityBinding
import com.example.bmicalculator.dialog.DeleteDialog
import com.example.bmicalculator.model.dto.Bmi
import com.example.bmicalculator.utils.SharePreferencesUtils
import kotlin.math.pow

class BMIActivity : BaseActivity<ActivityBmiactivityBinding>() {

    private var bmi: Bmi?=null
    private var weightMin = 47.3
    private var weightMax = 63.9
    private var weightUnit :String?= null
    private var heightUnit : String?= null
    private var weightDifference : Double?= 0.0
    private val viewModel: BmiViewModel by viewModels {
        BmiViewModelFactory((application as Application).repository)
    }
    lateinit var dialog : DeleteDialog
    private val isRecent by lazy {
        SharePreferencesUtils.getBoolean(Constance.ISRECENT,false)
    }

    override fun setViewBinding(): ActivityBmiactivityBinding {
        return ActivityBmiactivityBinding.inflate(layoutInflater)
    }

    override fun init() {
        initData()
        initView()
        initListener()
    }
    private fun initData(){
        val intent = intent
        var height : Double = 0.0
        var weight : Double

        if (intent.hasExtra(Constance.BMI)) {
            bmi = intent.getSerializableExtra(Constance.BMI) as Bmi
        }
        if (isRecent!!){
            weightUnit = bmi!!.weight.substringAfter(" ")
        }else{
            weightUnit =  intent.getStringExtra(Constance.WEIGHT)
        }

        heightUnit = bmi!!.height.substringAfter(" ")

        if (heightUnit == getString(R.string.cm)){
            height = bmi!!.height.substringBefore(" ").toDouble() / 100
        }else if (heightUnit == getString(R.string.ft)){
            height = bmi!!.height.substringBefore(" ").toDouble() * 0.3048
        }

        if (weightUnit == getString(R.string.kg)){
            weightMin = 18.5 * (height).pow(2.0)
            weightMax = 25 * (height).pow(2.0)
        }else if (weightUnit == getString(R.string.lb)){
            weightMin = 18.5 * (height).pow(2.0) * 2.205
            weightMax = 25 * (height).pow(2.0) * 2.205
        }else if (weightUnit == getString(R.string.st)){
            weightMin = 18.5 * (height).pow(2.0) * 0.157473
            weightMax = 25 * (height).pow(2.0) * 0.157473
        }

        weightMin = weightMin.toString().replace(",",".").toDouble()
        weightMin = Math.round(weightMin *10)/10.toDouble()
        weightMax = weightMax.toString().replace(",",".").toDouble()
        weightMax = Math.round(weightMax *10)/10.toDouble()
        if (bmi?.age!! >= 21){
            if (bmi?.bmi!! < 16){
                binding!!.tvStatusBmi.setBackgroundResource(R.drawable.custom_bg_status_very_severely_underweight)
                binding!!.tvStatusBmi.text = getString(R.string.tv_des_16)
            }else if (bmi?.bmi!! >= 16 && bmi?.bmi!! < 16.9){
                binding!!.tvStatusBmi.setBackgroundResource(R.drawable.custom_bg_status_severely_underweight)
                binding!!.tvStatusBmi.text = getString(R.string.severely_underweight)
            }else if (bmi?.bmi!! >= 17 && bmi?.bmi!! < 18.4){
                binding!!.tvStatusBmi.setBackgroundResource(R.drawable.custom_bg_status_underweight)
                binding!!.tvStatusBmi.text = getString(R.string.underweight)
            }else if (bmi?.bmi!! >= 18.5 && bmi?.bmi!! < 24.9){
                binding!!.tvStatusBmi.setBackgroundResource(R.drawable.custom_bg_status_normal)
                binding!!.tvStatusBmi.text = getString(R.string.normal)
            }else if (bmi?.bmi!! >= 25 && bmi?.bmi!! < 29.9){
                binding!!.tvStatusBmi.setBackgroundResource(R.drawable.custom_bg_status_overweight)
                binding!!.tvStatusBmi.text = getString(R.string.overweight)
            }else if (bmi?.bmi!! >= 30 && bmi?.bmi!! < 34.9){
                binding!!.tvStatusBmi.setBackgroundResource(R.drawable.custom_bg_status_obese_class_i)
                binding!!.tvStatusBmi.text = getString(R.string.obese_Class_I)
            }else if (bmi?.bmi!! >= 35 && bmi?.bmi!! < 39.9){
                binding!!.tvStatusBmi.setBackgroundResource(R.drawable.custom_bg_status_obese_class_ii)
                binding!!.tvStatusBmi.text = getString(R.string.obese_Class_II)
            }else if (bmi?.bmi!! > 40){
                binding!!.tvStatusBmi.setBackgroundResource(R.drawable.custom_bg_status_obese_class_iii)
                binding!!.tvStatusBmi.text = getString(R.string.obese_class_III)
            }
        }else if (bmi?.age!! <= 20){

            if (weightUnit == getString(R.string.kg)){
                weightMin = 15.4 * (height).pow(2.0)
                weightMax = 22.6 * (height).pow(2.0)
            }else if (weightUnit == getString(R.string.lb)){
                weightMin = 15.4 * (height).pow(2.0) * 2.205
                weightMax = 22.6 * (height).pow(2.0) * 2.205
            }else if (weightUnit == getString(R.string.st)){
                weightMin = 15.4 * (height).pow(2.0) * 0.157473
                weightMax = 22.6 * (height).pow(2.0) * 0.157473
            }

            weightMin = weightMin.toString().replace(",",".").toDouble()
            weightMin = Math.round(weightMin *10)/10.toDouble()
            weightMax = weightMax.toString().replace(",",".").toDouble()
            weightMax = Math.round(weightMax *10)/10.toDouble()

            if (bmi?.bmi!! < 15.4){
                binding!!.tvStatusBmi.setBackgroundResource(R.drawable.custom_bg_status_underweight20)
                binding!!.tvStatusBmi.text = getString(R.string.underweight)
            }else if (bmi?.bmi!! >= 15.4 && bmi?.bmi!! < 22.5){
                binding!!.tvStatusBmi.setBackgroundResource(R.drawable.custom_bg_normal20)
                binding!!.tvStatusBmi.text = getString(R.string.normal)
            }else if (bmi?.bmi!! >= 22.6 && bmi?.bmi!! < 26.3){
                binding!!.tvStatusBmi.setBackgroundResource(R.drawable.custom_bg_overweight20)
                binding!!.tvStatusBmi.text = getString(R.string.overweight)
            }else if (bmi?.bmi!! >= 26.4){
                binding!!.tvStatusBmi.setBackgroundResource(R.drawable.custom_bg_status_obese_class_i20)
                binding!!.tvStatusBmi.text = getString(R.string.obese_Class_I)
            }
        }

        if (bmi!!.weight.substringBefore(" ").toDouble() >= weightMin &&  bmi!!.weight.substringBefore(" ").toDouble() <= weightMax){
            weightDifference = 0.0
        }else if (bmi!!.weight.substringBefore(" ").toDouble() < weightMin){
            weightDifference = weightMin - bmi!!.weight.substringBefore(" ").toDouble()
            } else if (bmi!!.weight.substringBefore(" ").toDouble() >weightMax){
                weightDifference = bmi!!.weight.substringBefore(" ").toDouble() - weightMax
        }
        weightDifference = weightDifference.toString().replace(",",".").toDouble()
        Log.d("kn",weightDifference.toString())
        weightDifference = Math.round(weightDifference!! *10)/10.toDouble()
    }
    @SuppressLint("SetTextI18n")
    private fun initView(){
        var swivelAngle = 0f
        val bmiIndex = bmi!!.bmi

        binding!!.tvStatusBmi.isSelected = true
        binding!!.gender.isSelected = true
        binding!!.height.isSelected = true
        binding!!.weight.isSelected = true
        if (bmi!!.age <= 20){
            binding!!.imgGraphBmi.setImageResource(R.drawable.graph_teenagers)
            if (bmiIndex < 16){
                swivelAngle = 20/15.4f * bmiIndex.toFloat()
            }else if(bmiIndex  >= 16 && bmiIndex < 20){
                swivelAngle = 68/20f * bmiIndex.toFloat()
            }else if (bmiIndex >= 20 && bmiIndex < 22.6){
                swivelAngle = 100/21f * bmiIndex.toFloat()
            }else if (bmiIndex >= 22.6 && bmiIndex < 24){
                swivelAngle = 113/22.6f * bmiIndex.toFloat()
            }else if (bmiIndex >= 24 && bmiIndex < 26.4){
                swivelAngle = 155/25f * bmiIndex.toFloat()
            }else if (bmiIndex >= 26.4 && bmiIndex < 27){
                swivelAngle = 166/26.4f * bmiIndex.toFloat()
            }else if (bmiIndex >= 27){
                swivelAngle = 180f
            }
        }else{
            binding!!.imgGraphBmi.setImageResource(R.drawable.graph_about_adults)

            if (bmiIndex <= 16.9){
                swivelAngle = 6/16f * bmiIndex.toFloat()
            }else  if (bmiIndex > 16.9 && bmiIndex <= 18.4){
                swivelAngle = 14/17f * bmiIndex.toFloat()
            }else if (bmiIndex >= 18.5 && bmiIndex <=19){
                swivelAngle = 20.5f/18.5f * bmiIndex.toFloat()
            }else if (bmiIndex > 19 && bmiIndex <= 21){
                swivelAngle = 40/21f * bmiIndex.toFloat()
            }else if (bmiIndex > 21 && bmiIndex < 25){
                swivelAngle = 57/24f * bmiIndex.toFloat()
            }else if (bmiIndex >= 25 && bmiIndex < 26){
                swivelAngle = 62.5f/25 * bmiIndex.toFloat()
            }else if (bmiIndex >= 26 && bmiIndex < 27){
                swivelAngle = 82/27f * bmiIndex.toFloat()
            }else if (bmiIndex >= 27 && bmiIndex < 30){
                swivelAngle = 90/28f * bmiIndex.toFloat()
            }else if (bmiIndex >= 30.0 && bmiIndex < 32){
                swivelAngle = 98f/30f * bmiIndex.toFloat()
            }else if (bmiIndex >= 32 && bmiIndex < 35){
                swivelAngle = 130/33f * bmiIndex.toFloat()
            }else if (bmiIndex >=35 && bmiIndex < 37){
                swivelAngle = 134/35f * bmiIndex.toFloat()
            }else if (bmiIndex >= 37 && bmiIndex < 40){
                swivelAngle = 160/39f * bmiIndex.toFloat()
            }else if (bmiIndex >= 40 && bmiIndex < 43){
                swivelAngle = 166/40f * bmiIndex.toFloat()
            }else if (bmiIndex >= 43){
                swivelAngle = 180f
            }
        }

        binding!!.icNeedle.animate().rotation(swivelAngle).setDuration(3000).start()

        if (isRecent!!){
            binding!!.btnSave.visibility = View.GONE
            binding!!.imgRecent.visibility = View.GONE
            binding!!.imgDelete.visibility = View.VISIBLE
        }
        binding!!.tvTimeBmi.text = bmi?.time.toString()
        binding!!.tvBmi.text = bmi?.bmi.toString()
        binding!!.tvAgeBmi.text = bmi?.age.toString()
        binding!!.tvHeightBmi.text = bmi?.height.toString()
        if (bmi!!.gender == 1){
            binding!!.tvGenderBmi.text = getString(R.string.tv_male)
        }else{
            binding!!.tvGenderBmi.text = getString(R.string.tv_female)
        }
        binding!!.tvWeightBmi.text = bmi?.weight.toString()
        binding!!.tvNormalWeightIndex.text = weightMin.toString() + " – " + weightMax.toString() + " " + weightUnit

        if (bmi!!.weight.substringBefore(" ").toDouble() < weightMin){
            binding!!.tvDifferenceIndex.text = "-" + weightDifference!!.toString() + " " + weightUnit
        } else if (bmi!!.weight.substringBefore(" ").toDouble() > weightMax){
            binding!!.tvDifferenceIndex.text = "+ " + weightDifference!!.toString() + " " + weightUnit
        }else{
            binding!!.tvDifferenceIndex.text = "0 " + weightUnit
        }

    }

    private fun initListener(){
        var isClickBack = false
        binding!!.imgBack.setOnClickListener{
            if (!isClickBack){
                isClickBack = true
                if (isRecent!!){
                    SharePreferencesUtils.putBoolean(Constance.ISRECENT,false)
                    finish()
                }else{
                    finish()
                    Handler(Looper.getMainLooper()).postDelayed({
                        isClickBack = false
                    },500)
                }

            }
        }

        var isClickSave = false
        binding!!.btnSave.setOnClickListener {
            if (!isClickSave){
                isClickSave = true
                viewModel.insert(bmi!!)
                val intent = Intent(this, RecentActivity::class.java)
                startActivity(intent)
                finish()
                SharePreferencesUtils.putBoolean(Constance.SAVE,true)
                Handler(Looper.getMainLooper()).postDelayed({
                    isClickSave =false
                },500)
            }
        }

        var isClickDelete = false
        binding!!.imgDelete.setOnClickListener {
            if (!isClickDelete){
                isClickDelete = true
                dialog = DeleteDialog(this, object : DeleteDialog.OnDeleteClickListener{
                    override fun onClickListener() {
                        viewModel.delete(bmi!!)
                        SharePreferencesUtils.putBoolean(Constance.ISRECENT,false)
                        finish()
                    }
                })
                dialog.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    isClickDelete =false
                },500)
            }
        }

        var isClickRecent = false
        binding!!.imgRecent.setOnClickListener {
                if (!isClickRecent){
                    isClickRecent = true
                    val intent = Intent(this, RecentActivity::class.java)
                    startActivity(intent)
                    Handler(Looper.getMainLooper()).postDelayed({
                        isClickRecent = false
                    },500)
                }
        }
    }

}