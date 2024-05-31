package com.example.bmicalculator.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bmicalculator.Constance
import com.example.bmicalculator.R
import com.example.bmicalculator.adapter.SettingAdapter
import com.example.bmicalculator.base.BaseFragment
import com.example.bmicalculator.data.data
import com.example.bmicalculator.databinding.FragmentSettingBinding
import com.example.bmicalculator.dialog.DialogRate
import com.example.bmicalculator.dialog.OnDialogGoneListener
import com.example.bmicalculator.model.ItemSettingModel
import com.example.bmicalculator.ui.activity.LanguageActivity
import com.example.bmicalculator.ui.activity.VersionActivity
import com.example.bmicalculator.utils.SharePreferencesUtils
import com.github.mikephil.charting.BuildConfig


class SettingFragment : BaseFragment<FragmentSettingBinding>(),OnDialogGoneListener,
    SettingAdapter.OnItemSettingClickListener {

    lateinit var settingAdapter: SettingAdapter
    private val dialogRate by lazy {
        DialogRate(requireActivity(),null,this)
    }
    private val layoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(layoutInflater)
    }
    override fun initView() {
        initAdapter()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun initAdapter(){
        settingAdapter =  SettingAdapter(activity!!,this)
        settingAdapter.setItems(data.itemsettinglist)
        if (SharePreferencesUtils.getBoolean(Constance.RATE, false)!!){
            settingAdapter.removeItem(2)
        }
        binding.rcvSetting.layoutManager = layoutManager
        binding.rcvSetting.adapter = settingAdapter
    }

    private fun openWebPage(url: String) {
//        val webpage = Uri.parse(url)
//        val intent = Intent(Intent.ACTION_VIEW, webpage)
        val intent = Uri.parse(url).buildUpon().build()
        startActivity(Intent(Intent.ACTION_VIEW, intent))
//            startActivity(intent)
    }

    fun shareApp(context: Context) {
        val pInfo: PackageInfo =
            requireActivity().getPackageManager().getPackageInfo(requireActivity().getPackageName(), 0)
        val appPackageName = pInfo.packageName

        val appName = context.getString(R.string.app_name)
        val shareBodyText = "https://play.google.com/store/apps/details?id=$appPackageName"

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE, appName)
            putExtra(Intent.EXTRA_TEXT, shareBodyText)
        }
        context.startActivity(Intent.createChooser(sendIntent, null))
    }

    override fun onDialogGone() {
        settingAdapter.removeItem(2)
        activity?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                )
    }

    override fun hideSystemBar() {
        activity?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                )

    }

    override fun onClickItemListener(item: ItemSettingModel?) {
        when(item!!.img){
            R.drawable.ic_lang -> {
                startActivity(Intent(activity,LanguageActivity::class.java))
            }
            R.drawable.ic_share -> {
                shareApp(requireActivity())
            }
            R.drawable.ic_rate -> {
                if (!SharePreferencesUtils.getBoolean(Constance.RATE,false)!!){
                    dialogRate.show()
                }
            }
            R.drawable.ic_version -> {
                startActivity(Intent(activity,VersionActivity::class.java))
            }
            R.drawable.ic_policy -> {
                openWebPage("https://sites.google.com/view/policy-trangers/home")
            }
        }
    }
}