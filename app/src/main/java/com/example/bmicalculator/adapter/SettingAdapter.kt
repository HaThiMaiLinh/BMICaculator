package com.example.bmicalculator.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.bmicalculator.base.BaseAdapter
import com.example.bmicalculator.databinding.ItemSettingBinding
import com.example.bmicalculator.model.ItemSettingModel

class SettingAdapter(val context : Context, val listener : OnItemSettingClickListener) : BaseAdapter<ItemSettingModel, ItemSettingBinding>() {
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemSettingBinding {
        return ItemSettingBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: ItemSettingBinding, item: ItemSettingModel, position: Int) {
        binding.tvSetting.text = context.getString(item.textSetting)
        binding.imgIcSetting.setImageResource(item.img)
        binding.tvSetting.isSelected = true
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

    interface OnItemSettingClickListener {
        fun onClickItemListener(item: ItemSettingModel?)
    }
}