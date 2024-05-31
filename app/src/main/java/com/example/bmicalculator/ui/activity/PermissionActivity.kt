package com.example.bmicalculator.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.TypefaceCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.toSpannable
import com.example.bmicalculator.Constance
import com.example.bmicalculator.R
import com.example.bmicalculator.base.BaseActivity
import com.example.bmicalculator.databinding.ActivityPermissionBinding
import com.example.bmicalculator.utils.SharePreferencesUtils

class PermissionActivity : BaseActivity<ActivityPermissionBinding>() {
    override fun setViewBinding(): ActivityPermissionBinding {
        return ActivityPermissionBinding.inflate(layoutInflater)
    }

    private val PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    private val PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val PERMISSION_READ_AUDIO = Manifest.permission.READ_MEDIA_AUDIO
    private val PERMISSION_READ_VIDEO = Manifest.permission.READ_MEDIA_VIDEO
    private val PERMISSION_READ_IMAGES = Manifest.permission.READ_MEDIA_IMAGES
    private val PERMISSION_POST_NOTIFICATION = Manifest.permission.POST_NOTIFICATIONS

    private val PERMISSION_REQ_STORAGE_CODE = 1
    private val PERMISSION_REQ_CODE_POST_NOTIFICATION = 2

    private var isStoragePermission = false
    private var isStorageClick = false
    private var isNotificationPermission = false
    private var isNotificationClick = false
    private var lastClickTime = 0L

    override fun init() {
        SharePreferencesUtils.putBoolean(Constance.PERMISSION,true)
        initView()
        initListener()
    }
    @SuppressLint("NewApi")
    private fun initView(){
//        val typeface = ResourcesCompat.getFont(this, R.font.poppins_bold)
//        val spannableString = SpannableString(HtmlCompat.fromHtml(getString(R.string.tv_des_permission), HtmlCompat.FROM_HTML_MODE_LEGACY))
//        spannableString.setSpan(TypefaceSpan(typeface!!), 0, 14, 0)
//        binding!!.tvDes.text = spannableString

        val typeface = ResourcesCompat.getFont(this, R.font.poppins_bold)
        val htmlString = getString(R.string.tv_des_permission)
        val htmlString2 = getString(R.string.tv_des_permission_12)


        if (Build.VERSION.SDK_INT >= 33){
            binding!!.perNotification.visibility = View.VISIBLE
            binding!!.perReadStorage.visibility = View.GONE
            val spannedHtml: Spanned = HtmlCompat.fromHtml(htmlString, HtmlCompat.FROM_HTML_MODE_LEGACY)

            val spannableString = SpannableString(spannedHtml)
            spannableString.setSpan(
                CustomTypefaceSpan(typeface),
                0,
                14,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding!!.tvDes.text = spannableString
        } else {
            binding!!.perNotification.visibility = View.GONE
            binding!!.perReadStorage.visibility = View.VISIBLE
            val spannedHtml: Spanned = HtmlCompat.fromHtml(htmlString2, HtmlCompat.FROM_HTML_MODE_LEGACY)

            val spannableString = SpannableString(spannedHtml)
            spannableString.setSpan(
                CustomTypefaceSpan(typeface),
                0,
                14,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding!!.tvDes.text = spannableString
        }
    }
    private class CustomTypefaceSpan(private val typeface: Typeface?) : StyleSpan(Typeface.BOLD) {
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.typeface = typeface
        }
    }

    private fun initListener(){
        binding!!.apply {
            btnGo.setOnClickListener {
                val currentClickTime = System.currentTimeMillis()
                if (currentClickTime - lastClickTime > 1500){
                    lastClickTime = currentClickTime
                    SharePreferencesUtils.putBoolean(Constance.FIRST_LOG_APP, true)
                    SharePreferencesUtils.putBoolean("first_time", true)
                    startActivity(Intent(this@PermissionActivity,MainActivity::class.java))
                    finish()
//                    if(Build.VERSION.SDK_INT >= 33){
//                        if (isNotificationPermission){
//                            SharePreferencesUtils.putBoolean(Constance.FIRST_LOG_APP, true)
//                            SharePreferencesUtils.putBoolean("first_time", true)
//                            startActivity(Intent(this@PermissionActivity,MainActivity::class.java))
//                            finish()
//                        } else {
//                            val builder = AlertDialog.Builder(this@PermissionActivity)
//                            builder.setMessage(getString(R.string.requested_permission_message))
//                                .setTitle(getString(R.string.requested_permission_title))
//                                .setCancelable(false)
//                                .setPositiveButton(getString(R.string.agree)) { p0, _ ->
//                                    p0.dismiss()
//                                }
//                            builder.show()
//                        }
//                    } else {
//                        if (isStoragePermission){
//                            SharePreferencesUtils.putBoolean(Constance.FIRST_LOG_APP, true)
//                            SharePreferencesUtils.putBoolean("first_time", true)
//                            startActivity(Intent(this@PermissionActivity,MainActivity::class.java))
//                            finish()
//                        } else {
//                            val builder = AlertDialog.Builder(this@PermissionActivity)
//                            builder.setMessage(getString(R.string.requested_permission_message))
//                                .setTitle(getString(R.string.requested_permission_title))
//                                .setCancelable(false)
//                                .setPositiveButton(getString(R.string.agree)) { p0, _ ->
//                                    p0.dismiss()
//                                }
//                            builder.show()
//                        }
//                    }
                }
            }

            imgPerReadStorage.setOnClickListener {
                if (!isStorageClick){
                    if (isStoragePermission) {
                        isNotificationPermission = false
                        imgPerReadStorage.setImageResource(  R.drawable.switch_on_p )
                    } else {

                        requestStorage()
                    }
                }

            }

            imgPerNotification.setOnClickListener {
                if (!isNotificationClick){
                    if (isNotificationPermission) {
                        isNotificationPermission = false
                        imgPerNotification.setImageResource(  R.drawable.switch_off_p )
                    } else {
                        requestPostNotification()
                    }
                }

            }
        }
    }

    private fun requestStorage(){
        if (Build.VERSION.SDK_INT <= 32) {
            if (checkSelfPermission(PERMISSION_READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(PERMISSION_WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            ) {
                isStorageClick = true
                isStoragePermission = true
                binding!!.imgPerReadStorage.setImageResource(  R.drawable.switch_on_p )
            } else {
                isStoragePermission = false
                binding!!.imgPerReadStorage.setImageResource(  R.drawable.switch_off_p )
                requestPermissions(
                    arrayOf(PERMISSION_READ_EXTERNAL_STORAGE, PERMISSION_WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQ_STORAGE_CODE
                )
            }
        } else {
            if (checkSelfPermission(PERMISSION_READ_AUDIO) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(PERMISSION_READ_IMAGES) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(PERMISSION_READ_VIDEO) == PackageManager.PERMISSION_GRANTED
            ) {
                isStoragePermission = true
                binding!!.imgPerReadStorage.setImageResource(  R.drawable.switch_on_p )
            } else {
                isStoragePermission = false
                binding!!.imgPerReadStorage.setImageResource(  R.drawable.switch_off_p )
                requestPermissions(
                    arrayOf(
                        PERMISSION_READ_AUDIO,
                        PERMISSION_READ_IMAGES,
                        PERMISSION_READ_VIDEO
                    ),
                    PERMISSION_REQ_STORAGE_CODE
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkSelfPermission(PERMISSION_READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        ) {
            binding!!.imgPerReadStorage.setImageResource(  R.drawable.switch_on_p )
        } else {
            binding!!.imgPerReadStorage.setImageResource(  R.drawable.switch_off_p )
        }

        if (checkSelfPermission(PERMISSION_POST_NOTIFICATION) != PackageManager.PERMISSION_GRANTED) {
            binding!!.imgPerNotification.setImageResource(  R.drawable.switch_off_p )
        } else {
            binding!!.imgPerNotification.setImageResource(  R.drawable.switch_on_p )
        }
    }

    private fun requestPostNotification() {
        if (checkSelfPermission(PERMISSION_POST_NOTIFICATION) != PackageManager.PERMISSION_GRANTED) {
            isNotificationPermission = false
            binding!!.imgPerNotification.setImageResource(  R.drawable.switch_off_p )
            requestPermissions(arrayOf(PERMISSION_POST_NOTIFICATION), PERMISSION_REQ_CODE_POST_NOTIFICATION)
        } else {
            isNotificationClick = true
            isNotificationPermission = true
            binding!!.imgPerNotification.setImageResource(  R.drawable.switch_on_p )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_REQ_STORAGE_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isStoragePermission = true
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                        checkFilesAccessPermission()
//                    }
                    isStorageClick = true
                    binding!!.imgPerReadStorage.setImageResource(  R.drawable.switch_on_p )
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage(getString(R.string.requested_permission_message))
                        .setTitle(getString(R.string.requested_permission_title))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.permission_setting)) { p0, _ ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                            p0.dismiss()
                        }
                    builder.show()
                }
            }
            PERMISSION_REQ_CODE_POST_NOTIFICATION -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage(getString(R.string.requested_permission_message))
                        .setTitle(getString(R.string.requested_permission_title))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.permission_setting)) { p0, _ ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                            p0.dismiss()
                        }
                    builder.show()
                } else {
                    isNotificationPermission = true
                    binding!!.imgPerNotification.setImageResource(  R.drawable.switch_on_p )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun checkFilesAccessPermission(){
        if (Environment.isExternalStorageManager()) {
            //Do nothing
        } else {
            //request for the permission
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            val uri: Uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
    }


}