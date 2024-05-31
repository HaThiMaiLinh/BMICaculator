package com.example.bmicalculator.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.bmicalculator.R
import com.example.bmicalculator.base.BaseAdapter
import com.example.bmicalculator.databinding.ItemRecentBinding
import com.example.bmicalculator.model.dto.Bmi

class RecentAdapter(val context : Context,val listener: OnItemRecentClickListener) :BaseAdapter<Bmi,ItemRecentBinding>() {
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemRecentBinding {
        return ItemRecentBinding.inflate(inflater, parent, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun bind(binding: ItemRecentBinding, item: Bmi, position: Int) {
        if (item.gender == 1){
            binding.tvGenderBmi.text = context.getString(R.string.tv_male)
        }else{
            binding.tvGenderBmi.text = context.getString(R.string.tv_female)
        }
        binding.tvBmiIndex.text = item.bmi.toString()
        binding.tvAgeBmi.text = item.age.toString()
        binding.tvWeightBmi.text = item.weight
        binding.tvHeightBmi.text = item.height
        binding.tvDateBmi.text = item.time
        binding.tvStatusBmiRecent.isSelected = true
        binding.gender.isSelected = true
        binding.weight.isSelected = true
        binding.height.isSelected = true
        binding.tvGenderBmi.isSelected = true
        binding.tvWeightBmi.isSelected = true
        binding.tvHeightBmi.isSelected = true


        binding.progressBmi.setBackgroundProgressColor(ContextCompat.getColor(context,R.color.bg_progress))

        if (item.age >= 21){
            binding.progressBmi.setProgress(item.bmi.toFloat() * 180 / 50)
            if (item.bmi < 16){
                binding.tvStatusBmiRecent.setBackgroundResource(R.drawable.custom_bg_status_very_severely_underweight)
                binding.tvStatusBmiRecent.text = context.getString(R.string.tv_des_16)
                binding.progressBmi.setForegroundProgressColor(ContextCompat.getColor(context,R.color.very_severely_underweight))
            }else if (item.bmi >= 16 && item.bmi < 17){
                binding.tvStatusBmiRecent.setBackgroundResource(R.drawable.custom_bg_status_severely_underweight)
                binding.tvStatusBmiRecent.text = context.getString(R.string.severely_underweight)
                binding.progressBmi.setForegroundProgressColor(ContextCompat.getColor(context,R.color.severely_underweight))
            }else if (item.bmi >= 17 && item.bmi < 18.5){
                binding.tvStatusBmiRecent.setBackgroundResource(R.drawable.custom_bg_status_underweight)
                binding.tvStatusBmiRecent.text = context.getString(R.string.underweight)
                binding.progressBmi.setForegroundProgressColor(ContextCompat.getColor(context,R.color.underweight))
            }else if (item.bmi >= 18.5 && item.bmi < 25){
                binding.tvStatusBmiRecent.setBackgroundResource(R.drawable.custom_bg_status_normal)
                binding.tvStatusBmiRecent.text = context.getString(R.string.normal)
                binding.progressBmi.setForegroundProgressColor(ContextCompat.getColor(context,R.color.normal))
            }else if (item.bmi >= 25 && item.bmi < 30){
                binding.tvStatusBmiRecent.setBackgroundResource(R.drawable.custom_bg_status_overweight)
                binding.tvStatusBmiRecent.text = context.getString(R.string.overweight)
                binding.progressBmi.setForegroundProgressColor(ContextCompat.getColor(context,R.color.overweight))
            }else if (item.bmi >= 30 && item.bmi < 35){
                binding.tvStatusBmiRecent.setBackgroundResource(R.drawable.custom_bg_status_obese_class_i)
                binding.tvStatusBmiRecent.text = context.getString(R.string.obese_Class_I)
                binding.progressBmi.setForegroundProgressColor(ContextCompat.getColor(context,R.color.obese_class_I))
            }else if (item.bmi >= 35 && item.bmi < 40){
                binding.tvStatusBmiRecent.setBackgroundResource(R.drawable.custom_bg_status_obese_class_ii)
                binding.tvStatusBmiRecent.text = context.getString(R.string.obese_Class_II)
                binding.progressBmi.setForegroundProgressColor(ContextCompat.getColor(context,R.color.obese_class_II))
            }else if (item.bmi > 40){
                binding.tvStatusBmiRecent.setBackgroundResource(R.drawable.custom_bg_status_obese_class_iii)
                binding.tvStatusBmiRecent.text = context.getString(R.string.obese_class_III)
                binding.progressBmi.setForegroundProgressColor(ContextCompat.getColor(context,R.color.obese_class_III))
            }
        }else if (item.age <= 20){
            binding.progressBmi.setProgress(item.bmi.toFloat() * 180 / 30)
            if (item.bmi < 15.4){
                binding.tvStatusBmiRecent.setBackgroundResource(R.drawable.custom_bg_status_underweight20)
                binding.tvStatusBmiRecent.text = context.getString(R.string.underweight)
                binding.progressBmi.setForegroundProgressColor(ContextCompat.getColor(context,R.color.underweight20))
            }else if (item.bmi >= 15.4 && item.bmi < 22.6){
                binding.tvStatusBmiRecent.setBackgroundResource(R.drawable.custom_bg_normal20)
                binding.tvStatusBmiRecent.text = context.getString(R.string.normal)
                binding.progressBmi.setForegroundProgressColor(ContextCompat.getColor(context,R.color.normal20))
            }else if (item.bmi >= 22.6 && item.bmi < 26.3){
                binding.tvStatusBmiRecent.setBackgroundResource(R.drawable.custom_bg_overweight20)
                binding.tvStatusBmiRecent.text = context.getString(R.string.overweight)
                binding.progressBmi.setForegroundProgressColor(ContextCompat.getColor(context,R.color.overweight20))
            }else if (item.bmi >= 26.4){
                binding.tvStatusBmiRecent.setBackgroundResource(R.drawable.custom_bg_status_obese_class_i20)
                binding.tvStatusBmiRecent.text = context.getString(R.string.obese_Class_I)
                binding.progressBmi.setForegroundProgressColor(ContextCompat.getColor(context,R.color.obese_class_I20))
            }

            Log.d("kan",binding.progressBmi.getProgressbarMaxscale().toString())
        }
        
        var isClick = false
        binding.root.setOnClickListener{
            if (!isClick){
                isClick = true
                listener.onClickItemListener(item)
                Handler(Looper.getMainLooper()).postDelayed({
                    isClick = false
                },500)
            }
        }
    }

    interface OnItemRecentClickListener {
        fun onClickItemListener(item: Bmi?)
    }
}