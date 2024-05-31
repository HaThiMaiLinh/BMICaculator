package com.example.bmicalculator.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.example.bmicalculator.R
import com.example.bmicalculator.databinding.DialogCalanderBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalenderDialog(context: Context,val time:String,val listener: OnCalenderClickListener):Dialog(context) {

    private val binding by lazy {
        DialogCalanderBinding.inflate(layoutInflater)
    }

    var date : String ?= null
    var currentDateInEnglish : String ?=null

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

        window?.attributes?.gravity = Gravity.CENTER
//        window?.setBackgroundDrawable(context.getDrawable(R.drawable.custum_dialog))
        date = getCurrentDateFormatted()
        currentDateInEnglish = getCurrentDateInEnglishdialog()

        binding.calender.setDate(getTimeInMillis(time),false,true)
    }

    private fun getTimeInMillis(time: String): Long {
        val timer = time.substringAfter(".")
        val day = time.substringBefore(".").toInt()
        val year = timer.substringAfter(".").toInt()
        val month = timer.substringBefore(".").toInt()
        val calendar = Calendar.getInstance()
        calendar.set(year, month -1, day)
        return calendar.timeInMillis
    }

    override fun show() {
        super.show()
        binding.tvOk.setOnClickListener{
            listener.onClickListener()
            dismiss()
        }
        binding.tvCancel.setOnClickListener{
            listener.onClickCancel()
            dismiss()
        }


        binding.calender.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            date = sdf.format(selectedDate.time)
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
            currentDateInEnglish = dateFormat.format(selectedDate.time)
        }

    }

    fun getCurrentDateInEnglishdialog(): String {
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
        return dateFormat.format(currentDate)
    }

    fun getCurrentDateFormatted(): String {
        val currentDate = Date()
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        return formatter.format(currentDate)
    }

    interface OnCalenderClickListener {
        fun onClickListener()
        fun onClickCancel()
    }

}