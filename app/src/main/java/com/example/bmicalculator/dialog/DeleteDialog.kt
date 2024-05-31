package com.example.bmicalculator.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.example.bmicalculator.R
import com.example.bmicalculator.databinding.DialogDeleteBinding

class DeleteDialog(context: Context, private val listener: OnDeleteClickListener) :Dialog(context) {

    private val binding by lazy {
        DialogDeleteBinding.inflate(layoutInflater)
    }

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
    }

    override fun show() {
        super.show()
        binding.btnCancel.setOnClickListener{
            dismiss()
        }
        binding.btnDelete.setOnClickListener{
            listener.onClickListener()
            dismiss()
        }
    }

    interface OnDeleteClickListener {
        fun onClickListener()
    }

    override fun dismiss() {
        super.dismiss()
    }

}