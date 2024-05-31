package com.example.bmicalculator.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bmicalculator.Constance
import com.example.bmicalculator.R
import com.example.bmicalculator.adapter.FilterAdapter
import com.example.bmicalculator.adapter.FilterYearAdapter
import com.example.bmicalculator.data.data
import com.example.bmicalculator.databinding.DialogMonthBinding
import com.example.bmicalculator.utils.SharePreferencesUtils
import java.time.LocalDate


class MonthDialog(context: Context,val listener: OnMonthClickListener):Dialog(context),FilterAdapter.IFilterItem,FilterYearAdapter.IFilterYearItem {

    private val binding by lazy {
        DialogMonthBinding.inflate(layoutInflater)
    }
    private val adapter  by lazy {
        FilterAdapter(context,data.filterlist,this)
    }
    private val adapter2 by lazy {
        FilterYearAdapter(context,data.filterYear,this)
    }

    var monthitem : String ?= context.getString(R.string.January)

    var year : Int = LocalDate.now().year
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window?.setBackgroundDrawableResource(R.drawable.custom_dialog)
        window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val drawable = GradientDrawable()
        drawable.cornerRadius = 16f  // Đặt độ cong theo ý muốn
        window?.setBackgroundDrawable(drawable)
        window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
        val layoutParams = window?.attributes
        layoutParams?.x = dpToPx(context,30f)
        layoutParams?.y = dpToPx(context,240f)
        window?.attributes?.gravity = Gravity.TOP or Gravity.RIGHT
        window?.attributes = layoutParams
//        window?.setBackgroundDrawable(context.getDrawable(R.drawable.custum_dialog))
        val layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rcvFilter.layoutManager = layoutManager

        if (SharePreferencesUtils.getBoolean(Constance.FILTER_YEAR,false)!!){
            binding.rcvFilter.adapter = adapter2
            adapter2.setCheck(year)
        }else{
            binding.rcvFilter.adapter = adapter
            if (monthitem != null){
                adapter.setCheck(monthitem)
            }
        }

    }

    fun dpToPx(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    override fun show() {
        super.show()

        binding.tvOk.setOnClickListener{
            listener.onClickListener()
            if (SharePreferencesUtils.getBoolean(Constance.FILTER_YEAR,false)!!){
                SharePreferencesUtils.putBoolean(Constance.FILTER_YEAR,false)
            }
            dismiss()
        }
        binding.tvCancel.setOnClickListener{
            if (SharePreferencesUtils.getBoolean(Constance.FILTER_YEAR,false)!!){
                SharePreferencesUtils.putBoolean(Constance.FILTER_YEAR,false)
            }
            listener.onHideSystemBar()
            dismiss()
        }

    }

    interface OnMonthClickListener {
        fun onClickListener()
        fun onHideSystemBar()
    }

    override fun onClickItemFilter(month: String?) {
        monthitem = month
    }

    override fun onClickItemFilter(year: Int?) {
        this.year = year!!
    }
}