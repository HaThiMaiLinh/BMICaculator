package com.example.bmicalculator.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.bmicalculator.Constance
import com.example.bmicalculator.R
import com.example.bmicalculator.base.BaseFragment
import com.example.bmicalculator.databinding.FragmentCalculatorBinding
import com.example.bmicalculator.dialog.CalenderDialog
import com.example.bmicalculator.model.dto.Bmi
import com.example.bmicalculator.ui.activity.BMIActivity
import com.example.bmicalculator.ui.activity.RecentActivity
import com.example.bmicalculator.utils.SharePreferencesUtils
import com.google.android.material.datepicker.MaterialDatePicker
import java.lang.reflect.Field
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class CalculatorFragment : BaseFragment<FragmentCalculatorBinding>(),CalenderDialog.OnCalenderClickListener {


    private val typefaceBold by lazy {
        ResourcesCompat.getFont(requireContext(), R.font.poppins_bold)
    }
    private val typefaceNormal by lazy {
        ResourcesCompat.getFont(requireContext(), R.font.poppins)
    }
    private lateinit var dialog : CalenderDialog
    private lateinit var date :String
    private lateinit var time :String
    private var gender : Int? = null
    private var age : Int? = null
    private var weight: Double? = null
    private var weightUnit: String?=null
    private var height : Double?= null
    private var heightUnit: String?=null
    private var bmiIndex : String?= null
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCalculatorBinding {
        return FragmentCalculatorBinding.inflate(inflater, container,false)
    }

    override fun initView() {
        date = getCurrentDateInEnglish()
        time = getCurrentDateFormatted()
        binding.tvDate.text = date
        gender = 1
        weightUnit = getString(R.string.kg)
        heightUnit = getString(R.string.cm)
        setupNumberPicker()
        setupNumberPickerWeight()
        setupNumberPickerHeight()
        initListener()
    }

    override fun onStart() {
        super.onStart()
        if(!SharePreferencesUtils.getBoolean(Constance.DATE_CHANGE,true)!!){
            date = getCurrentDateInEnglish()
            time = getCurrentDateFormatted()
            binding.tvDate.text = date
        }
        hideSystemUI()
        SharePreferencesUtils.putBoolean(Constance.DATE_CHANGE,false)
    }

    @SuppressLint("ResourceAsColor", "SuspiciousIndentation")
    private fun initListener(){
        binding.Male.setOnClickListener(View.OnClickListener {
            hideKeyboard()
            gender = 1
            binding.Male.background = activity?.let { it1 -> ContextCompat.getDrawable(it1,R.drawable.bg_gender_select) }
            binding.Female.background = activity?.let { it1 -> ContextCompat.getDrawable(it1,R.drawable.bg_gender_unselect) }
            binding.imgMale.setImageResource(R.drawable.ic_male_select)
            binding.tvMale.setTextColor(ContextCompat.getColor(requireActivity(),R.color.white))
            binding.imgFemale.setImageResource(R.drawable.ic_female_unselect)
            binding.tvFemale.setTextColor(ContextCompat.getColor(requireActivity(),R.color.tv_item))
        })
        binding.Female.setOnClickListener(View.OnClickListener {
            hideKeyboard()
            gender = 0
            binding.Female.background = activity?.let { it1 -> ContextCompat.getDrawable(it1!!,R.drawable.bg_gender_select) }
            binding.Male.background = activity?.let { it1 -> ContextCompat.getDrawable(it1!!,R.drawable.bg_gender_unselect) }
            binding.imgMale.setImageResource(R.drawable.ic_male_unselect)
            binding.tvMale.setTextColor(ContextCompat.getColor(requireActivity(),R.color.tv_item))
            binding.imgFemale.setImageResource(R.drawable.ic_female_select)
            binding.tvFemale.setTextColor(ContextCompat.getColor(requireActivity(),R.color.white))
        })
        binding.imgReload.setOnClickListener {
            hideKeyboard()
            binding.tvDate.text = date
            gender = 1
            binding.Male.background = activity?.let { it1 -> ContextCompat.getDrawable(it1,R.drawable.bg_gender_select) }
            binding.Female.background = activity?.let { it1 -> ContextCompat.getDrawable(it1,R.drawable.bg_gender_unselect) }
            binding.imgMale.setImageResource(R.drawable.ic_male_select)
            binding.tvMale.setTextColor(ContextCompat.getColor(requireActivity(),R.color.white))
            binding.imgFemale.setImageResource(R.drawable.ic_female_unselect)
            binding.tvFemale.setTextColor(ContextCompat.getColor(requireActivity(),R.color.tv_item))
            binding.numberPicker.value = 20
            binding.edtHeight.text.clear()
            binding.edtWeight.text.clear()
            binding.numberPickerWeight.value = 2
            binding.numberPickerHeight.value = 2
        }
        binding.bgCalculator.setOnClickListener(View.OnClickListener {
           if(binding.edtHeight.text.toString().isNotEmpty()){
               isValueHeight()
           }
            if ( binding.edtWeight.text.toString().isNotEmpty()){
                isValueWeight()
            }
            hideKeyboard()
            hideSystemUI()
        })
        binding.edtWeight.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                isValueWeight()
                if (binding.edtHeight.text.toString().isNotEmpty() ){
                    isValueHeight()
                }
                binding.edtWeight.clearFocus()
                hideSystemUI()
                return@setOnEditorActionListener false
            }
            return@setOnEditorActionListener true
        }

        binding.edtHeight.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
               isValueHeight()
               if (binding.edtWeight.text.toString().isNotEmpty()){
                   isValueWeight()
               }
                binding.edtHeight.clearFocus()
                hideSystemUI()
                return@setOnEditorActionListener false
            }
            return@setOnEditorActionListener true
        }

        var isClickCalculator = false
        binding.btnCalculator.setOnClickListener(View.OnClickListener {
            // 1lb = 0.45359237kg ,1 foot (feet) = 30,48 cm 1st= 6.350293kg
            // BMI = cân nặng (kg) / chiều cao² (m)
            if (!isClickCalculator){
                isClickCalculator = true
                age = binding.numberPicker.value

                if (gender != null && binding.edtWeight.text.toString().trim().isNotEmpty() &&
                    binding.edtHeight.text.toString().trim().isNotEmpty()
                    && isValueHeight() && isValueWeight()){
                    weight = binding.edtWeight.text.toString().trim().toDouble()
                    height = binding.edtHeight.text.toString().trim().toDouble()
                    when(binding.numberPickerWeight.value){
                        1 ->{
                            weight = weight!! * 0.45359237
                            weightUnit = getString(R.string.lb)
                        }
                        2 ->{
                            weight = weight!!
                            weightUnit = getString(R.string.kg)
                        }
                        3 ->{
                            weight = weight!! * 6.350293
                            weightUnit = getString(R.string.st)
                        }
                    }
                    when(binding.numberPickerHeight.value){
                        1 ->{
                            height = height!! * 30.48
                            heightUnit = getString(R.string.ft)
                        }
                        2 ->{
                            height = height!!
                            heightUnit = getString(R.string.cm)
                        }
                    }

                    val decimalFormat = DecimalFormat("#.#")
                    var Bmi = weight!! / Math.pow((height!! /100),2.0)
                    bmiIndex = decimalFormat.format(Bmi)

                    var weightbmi = binding.edtWeight.text.toString() + " " + weightUnit
                    var heightbmi = binding.edtHeight.text.toString() + " " + heightUnit
                    bmiIndex = bmiIndex.toString().replace(",",".")
                    var bmi = Bmi(time, gender!!,age!!,weightbmi,heightbmi,bmiIndex!!.toDouble())
                    if (bmiIndex!!.toDouble() > 70){
                        Toast.makeText(activity,getString(R.string.bmi_error),Toast.LENGTH_SHORT).show()
                    }else{
                        SharePreferencesUtils.putBoolean(Constance.DATE_CHANGE,true)
                        val intent = Intent(activity, BMIActivity::class.java)
                        intent.putExtra(Constance.BMI,bmi)
                        intent.putExtra(Constance.WEIGHT,weightUnit)
                        startActivity(intent)
                    }

                }else{
                    isGender()
                    isValueHeight()
                    isValueWeight()
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    isClickCalculator = false
                },500)
            }
        })

        var isClickRecent = false
        binding.imgRecent.setOnClickListener {
            if (!isClickRecent){
                isClickRecent = true
                SharePreferencesUtils.putBoolean(Constance.DATE_CHANGE,true)
                val intent = Intent(activity, RecentActivity::class.java)
                startActivity(intent)
                Handler(Looper.getMainLooper()).postDelayed({
                    isClickRecent = false
                },500)
            }
        }
        var isClickDatePicker = false
        binding.imgDatePicker.setOnClickListener {
            if (!isClickDatePicker){
                isClickDatePicker = true
                dialog = CalenderDialog(requireActivity(),time,this)
                dialog.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    isClickDatePicker = false
                },500)
            }

        }
    }

//    fun checkdate():Boolean{
//        val currentDate = getCurrentDateFormatted().substringBefore(".").toInt()
//        val currentTimer = getCurrentDateFormatted().substringAfter(".")
//        val currentYear = currentTimer.substringAfter(".").toInt()
//        val currentMonth = currentTimer.substringBefore(".").toInt()
//        val dateSelect = time.substringBefore(".").toInt()
//        val timer = time.substringAfter(".")
//        val year = timer.substringAfter(".").toInt()
//        val month = timer.substringBefore(".").toInt()
//        if (currentDate - dateSelect > 3 || currentYear > year || currentMonth > month) {
//            Toast.makeText(activity,getString(R.string.toast_date),Toast.LENGTH_SHORT).show()
//            return false
//        }
//        return true
//    }

    fun hideSystemUI() {
        requireActivity().window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        binding.edtWeight.clearFocus()
        binding.edtHeight.clearFocus()
    }

    fun getCurrentDateInEnglish(): String {
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        return dateFormat.format(currentDate)
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateFormatted(): String {
        val currentDate = Date()
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        return formatter.format(currentDate)
    }

    private fun setupNumberPicker(){
        binding.numberPicker.setTypeface(typefaceBold)
        binding.numberPicker.setWrapSelectorWheel(true)
        binding.numberPicker.setScrollerEnabled(true)
        binding.numberPicker.setSelectedTypeface(typefaceBold)
    }

    private fun setupNumberPickerWeight(){
        val data = arrayOf(getString(R.string.lb), getString(R.string.kg), getString(R.string.st))
        binding.numberPickerWeight.setMinValue(1)
        binding.numberPickerWeight.setMaxValue(data.size)
        binding.numberPickerWeight.setDisplayedValues(data)
        binding.numberPickerWeight.setTypeface(typefaceNormal)
        binding.numberPickerWeight.setWrapSelectorWheel(true)
        binding.numberPickerWeight.setScrollerEnabled(true)
        binding.numberPickerWeight.setSelectedTypeface(typefaceBold)
    }

    private fun setupNumberPickerHeight(){
        val data1 = arrayOf(getString(R.string.ft), getString(R.string.cm))
        binding.numberPickerHeight.setMinValue(1);
        binding.numberPickerHeight.setMaxValue(data1.size)
        binding.numberPickerHeight.setDisplayedValues(data1)
        binding.numberPickerHeight.setTypeface(typefaceNormal)
        binding.numberPickerHeight.setWrapSelectorWheel(true)
        binding.numberPickerHeight.setScrollerEnabled(true)
        binding.numberPickerHeight.setSelectedTypeface(typefaceBold)
    }

    private fun isValueWeight() :Boolean{
        if (binding.edtWeight.text.toString().trim().isEmpty() || binding.tvWeight.text.toString().trim() == ""){
            Toast.makeText(activity,context?.getString(R.string.is_value_weight),Toast.LENGTH_SHORT).show()
            return false
        }else{
            weight = binding.edtWeight.text.toString().trim().toDouble()
            when(binding.numberPickerWeight.value) {
                1 -> {
                    if (weight!! < 2.2 || weight!! > 661.39){
                        binding.edtWeight.setText("2.2")
                        Toast.makeText(activity,getString(R.string.toast_lb),Toast.LENGTH_SHORT).show()
                        return false
                    }
                }
                2 -> {
                    if (weight!! < 1 || weight!! >300) {
                        binding.edtWeight.setText("1.00")
                        Toast.makeText(activity,getString(R.string.enter_error),Toast.LENGTH_SHORT).show()
                        return false
                    }
                }
                3 -> {
                    if (weight!! < 0.157 || weight!! > 47.24){
                        binding.edtWeight.setText("0.157")
                        Toast.makeText(activity,getString(R.string.toast_st),Toast.LENGTH_SHORT).show()
                        return false
                    }
                }
            }
        }
        return true
    }

    @SuppressLint("SuspiciousIndentation")
    private fun isValueHeight() :Boolean{
        if (binding.edtHeight.text.toString().trim().isEmpty() || binding.tvHeight.text.toString().trim() == ""){
            Toast.makeText(activity,context?.getString(R.string.is_value_height),Toast.LENGTH_SHORT).show()
            return false
        }else{
            height = binding.edtHeight.text.toString().trim().toDouble()
                when(binding.numberPickerHeight.value) {
                    1 -> {
                        if (height!! <= 0 || height!! > 9.842){
                            binding.edtHeight.setText("0.033")
                            Toast.makeText(activity,getString(R.string.toast_ft),Toast.LENGTH_SHORT).show()
                            return false
                        }
                    }
                    2 -> {
                        if (height!! < 1 || height!! > 300){
                            binding.edtHeight.setText("1.00")
                            Toast.makeText(activity,getString(R.string.toast_cm),Toast.LENGTH_SHORT).show()
                            return false
                        }
                    }
            }
        }
        return true
    }

    private fun isGender(){
        if (gender == null){
            Toast.makeText(context,context?.getString(R.string.is_value_gender),Toast.LENGTH_SHORT).show()
        }
    }


    override fun onClickListener() {
        date = dialog.currentDateInEnglish!!
        time = dialog.date!!
        binding.tvDate.text = date
        hideSystemUI()
    }

    override fun onClickCancel() {
        hideSystemUI()
    }

}