package com.example.bmicalculator.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.example.bmicalculator.Constance
import com.example.bmicalculator.R
import com.example.bmicalculator.databinding.DialogRateBinding
import com.example.bmicalculator.utils.SharePreferencesUtils
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode

class DialogRate(context: Context, private val dismissListener: OnDialogDismissListener? = null,private val goneListener: OnDialogGoneListener ?= null) : Dialog(context) {
    var rating: Float = 0F
    private val bindingDialogRate by lazy {
        DialogRateBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindingDialogRate.root)
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

    fun hideSystemUI() {
        this.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                )
    }

    override fun show() {
        super.show()
        bindingDialogRate.ratingBar.rating = 0f
        bindingDialogRate.btnRate.setOnClickListener {
            if (rating > 3){
                SharePreferencesUtils.putBoolean(Constance.RATE, true)
                goneListener?.onDialogGone()
//                reviewApp(context)
            }
            dismiss()
        }

        bindingDialogRate.tvExit.setOnClickListener {
            goneListener?.hideSystemBar()
            dismiss()
        }

        bindingDialogRate.ratingBar.setOnRatingChangeListener { ratingBar, rating, fromUser ->
            this.rating = rating
            when (rating) {
                0F -> {
                    bindingDialogRate.img.setImageResource(R.drawable.rate_0)
                    bindingDialogRate.tvTitle.text = context.getString(R.string.title_rate_0)
                    bindingDialogRate.tvDes.text = context.getString(R.string.des_rate_0)

                }

                1F -> {
                    bindingDialogRate.img.setImageResource(R.drawable.rate_1)
                    bindingDialogRate.tvTitle.text = context.getString(R.string.title_rate_1)
                    bindingDialogRate.tvDes.text = context.getString(R.string.des_rate_1)
                }

                2F -> {
                    bindingDialogRate.img.setImageResource(R.drawable.rate_2)
                    bindingDialogRate.tvTitle.text = context.getString(R.string.title_rate_2)
                    bindingDialogRate.tvDes.text = context.getString(R.string.des_rate_2)
                }

                3F -> {
                    bindingDialogRate.img.setImageResource(R.drawable.rate_3)
                    bindingDialogRate.tvTitle.text = context.getString(R.string.title_rate_3)
                    bindingDialogRate.tvDes.text = context.getString(R.string.des_rate_3)
                }

                4F -> {
                    bindingDialogRate.img.setImageResource(R.drawable.rate_4)
                    bindingDialogRate.tvTitle.text = context.getString(R.string.title_rate_4)
                    bindingDialogRate.tvDes.text = context.getString(R.string.des_rate_4)
                }

                5F -> {
                    bindingDialogRate.img.setImageResource(R.drawable.rate_5)
                    bindingDialogRate.tvTitle.text = context.getString(R.string.title_rate_5)
                    bindingDialogRate.tvDes.text = context.getString(R.string.des_rate_5)
                }
            }
        }
    }

    fun rateApp() {
        val manager = ReviewManagerFactory.create(context)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(context as Activity, reviewInfo)
                flow.addOnCompleteListener { _ ->
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                }
            } else {
                // There was some problem, log or handle the error code.
                @ReviewErrorCode val reviewErrorCode =
                    (task.exception as ReviewException).errorCode
            }
        }
    }

    fun reviewApp(context: Context) {
        val manager: ReviewManager = ReviewManagerFactory.create(context)
        val request: Task<ReviewInfo> = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We can get the ReviewInfo object
                val reviewInfo = task.result
                Log.e("ReviewInfo", "" + reviewInfo.toString())
                val flow: Task<Void> = manager.launchReviewFlow(context as Activity, reviewInfo)
                flow.addOnCompleteListener { task2 ->
                    Log.e("ReviewSucces", "" + task2.toString())
                    // checkExiting(context, interstitialAd)
                    System.exit(0)
                }
            } else {
                // There was some problem, continue regardless of the result.
                // Log.e("ReviewError", "" + task.exception.toString())
                System.exit(0)
            }
        }
    }


    override fun dismiss() {
        super.dismiss()
        Log.d("DialogDismiss", "Dialog dismissed")
        dismissListener?.onDialogDismiss()
    }
}

interface OnDialogDismissListener {
    fun onDialogDismiss()
}
interface OnDialogGoneListener {
    fun onDialogGone()
    fun hideSystemBar()
}
