package com.example.bmicalculator.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.bmicalculator.Constance
import com.example.bmicalculator.R
import com.example.bmicalculator.ViewModel.BmiViewModel
import com.example.bmicalculator.ViewModel.BmiViewModelFactory
import com.example.bmicalculator.base.Application
import com.example.bmicalculator.base.BaseFragment
import com.example.bmicalculator.databinding.FragmentMonthlyBinding
import com.example.bmicalculator.dialog.MonthDialog
import com.example.bmicalculator.utils.SharePreferencesUtils
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.time.LocalDate
import java.time.Month


class MonthlyFragment : BaseFragment<FragmentMonthlyBinding>(),MonthDialog.OnMonthClickListener {


    private val viewModel: BmiViewModel by viewModels {
        BmiViewModelFactory((requireActivity().application as Application).repository)
    }
    private val dialog: MonthDialog by lazy {
        MonthDialog(requireContext(),this)
    }
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMonthlyBinding {
        return FragmentMonthlyBinding.inflate(layoutInflater)
    }

    override fun initView() {
        displayChart()
        displayChartWeight()
        initListener()
        binding.tvYear.text = LocalDate.now().year.toString()
        binding.tvYearWeight.text = LocalDate.now().year.toString()
    }
    private fun initListener(){
        var isClickfilter = false
        binding.filterChart.setOnClickListener {
            if (!isClickfilter){
                isClickfilter = true
                SharePreferencesUtils.putBoolean(Constance.FILTER_YEAR,true)
                dialog.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    isClickfilter =false
                },500)
            }
        }
    }

    private fun displayChart() {
        val values = mutableListOf<Entry>()
        val January = mutableListOf<Float>()
        val February = mutableListOf<Float>()
        val March = mutableListOf<Float>()
        val April = mutableListOf<Float>()
        val May = mutableListOf<Float>()
        val June = mutableListOf<Float>()
        val July = mutableListOf<Float>()
        val August = mutableListOf<Float>()
        val September = mutableListOf<Float>()
        val October = mutableListOf<Float>()
        val November = mutableListOf<Float>()
        val December = mutableListOf<Float>()
        val xAxis: XAxis = binding.lineChartBmi.xAxis
        val yAxis: YAxis = binding.lineChartBmi.axisLeft
        val maxX = 12f
        viewModel.allBmi.observe( this){listbmi->
            for (it in listbmi){
                val time = it.time.substringAfter(".")
                val month = time.substringBefore(".").toInt()
                val year = time.substringAfter(".").toInt()

                if (year == binding.tvYear.text.toString().toInt()){
                    when(month){
                        1-> January.add(it.bmi.toFloat())
                        2-> February.add(it.bmi.toFloat())
                        3-> March.add(it.bmi.toFloat())
                        4-> April.add(it.bmi.toFloat())
                        5-> May.add(it.bmi.toFloat())
                        6-> June.add(it.bmi.toFloat())
                        7-> July.add(it.bmi.toFloat())
                        8-> August.add(it.bmi.toFloat())
                        9-> September.add(it.bmi.toFloat())
                        10-> October.add(it.bmi.toFloat())
                        11-> November.add(it.bmi.toFloat())
                        12-> December.add(it.bmi.toFloat())
                    }
                }
            }

            if (January.size > 0){
                values.add(Entry(1f,January.average().toFloat()))
            }
            if (February.size > 0){
                values.add(Entry(2f,February.average().toFloat()))
            }
            if (March.size > 0){
                values.add(Entry(3f,March.average().toFloat()))
            }
            if (April.size > 0){
                values.add(Entry(4f,April.average().toFloat()))
            }
            if (May.size > 0){
                values.add(Entry(5f,May.average().toFloat()))
            }
            if (June.size > 0){
                values.add(Entry(6f,June.average().toFloat()))
            }
            if (July.size > 0){
                values.add(Entry(7f,July.average().toFloat()))
            }
            if (August.size > 0){
                values.add(Entry(8f,August.average().toFloat()))
            }
            if (September.size > 0){
                values.add(Entry(9f,September.average().toFloat()))
            }
            if (October.size > 0){
                values.add(Entry(10f,October.average().toFloat()))
            }
            if (November.size > 0){
                values.add(Entry(11f,November.average().toFloat()))
            }
            if (December.size > 0){
                values.add(Entry(12f,December.average().toFloat()))
            }
            val dataSet = LineDataSet(values, "Label")
            dataSet.setDrawValues(true)
            dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
            dataSet.cubicIntensity = 0.2f
            dataSet.color = ContextCompat.getColor(requireContext(), R.color.white)
            dataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.white)

            val dataSets = mutableListOf<ILineDataSet>()
            dataSets.add(dataSet)

            val lineData = LineData(dataSets)
            binding.lineChartBmi.data = lineData

            binding.lineChartBmi.description.isEnabled = false
            binding.lineChartBmi.legend.isEnabled = false
            binding.lineChartBmi.setTouchEnabled(true)
            binding.lineChartBmi.isDragEnabled = true
            binding.lineChartBmi.setScaleEnabled(true)
            binding.lineChartBmi.setPinchZoom(false)
            binding.lineChartBmi.axisRight.isEnabled = false
            binding.lineChartBmi.axisRight.setDrawLabels(false)
            binding.lineChartBmi.xAxis.isEnabled = true
            binding.lineChartBmi.xAxis.setDrawLabels(true)
            binding.lineChartBmi.xAxis.position = XAxis.XAxisPosition.BOTTOM
            binding.lineChartBmi.moveViewToX(0f)
            xAxis.setDrawGridLines(false)
            xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)
            xAxis.axisLineColor = ContextCompat.getColor(requireContext(), R.color.transfer)
            xAxis.axisMinimum = 0f
            xAxis.axisMaximum = maxX
            xAxis.granularity = 1f
            yAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)
            yAxis.axisMinimum = 0f
            yAxis.axisMaximum = 80f
            yAxis.axisLineColor =  ContextCompat.getColor(requireContext(), R.color.transfer)
            binding.lineChartBmi.setVisibleXRangeMinimum(4f)
            binding.lineChartBmi.setVisibleXRangeMaximum(4f)
            binding.lineChartBmi.setVisibleYRangeMaximum(80f,YAxis.AxisDependency.LEFT)
            binding.lineChartBmi.invalidate()
        }

    }

    private fun displayChartWeight() {
        val values = mutableListOf<Entry>()

        val January = mutableListOf<Float>()
        val February = mutableListOf<Float>()
        val March = mutableListOf<Float>()
        val April = mutableListOf<Float>()
        val May = mutableListOf<Float>()
        val June = mutableListOf<Float>()
        val July = mutableListOf<Float>()
        val August = mutableListOf<Float>()
        val September = mutableListOf<Float>()
        val October = mutableListOf<Float>()
        val November = mutableListOf<Float>()
        val December = mutableListOf<Float>()
        val xAxis: XAxis = binding.lineChartWeight.xAxis
        val yAxis: YAxis = binding.lineChartWeight.axisLeft
        val maxX = 12f
        viewModel.allBmi.observe( this){listbmi->
            for (it in listbmi){
                val time = it.time.substringAfter(".")
                val month = time.substringBefore(".").toInt()
                val year = time.substringAfter(".").toInt()
                val weight = it.weight.substringBefore(" ").toFloat()
                if (year == binding.tvYear.text.toString().toInt()){
                    when(month){
                        1-> January.add(weight)
                        2-> February.add(weight)
                        3-> March.add(weight)
                        4-> April.add(weight)
                        5-> May.add(weight)
                        6-> June.add(weight)
                        7-> July.add(weight)
                        8-> August.add(weight)
                        9-> September.add(weight)
                        10-> October.add(weight)
                        11-> November.add(weight)
                        12-> December.add(weight)
                    }
                }
            }

            if (January.size > 0){
                values.add(Entry(1f,January.average().toFloat()))
            }
            if (February.size > 0){
                values.add(Entry(2f,February.average().toFloat()))
            }
            if (March.size > 0){
                values.add(Entry(3f,March.average().toFloat()))
            }
            if (April.size > 0){
                values.add(Entry(4f,April.average().toFloat()))
            }
            if (May.size > 0){
                values.add(Entry(5f,May.average().toFloat()))
            }
            if (June.size > 0){
                values.add(Entry(6f,June.average().toFloat()))
            }
            if (July.size > 0){
                values.add(Entry(7f,July.average().toFloat()))
            }
            if (August.size > 0){
                values.add(Entry(8f,August.average().toFloat()))
            }
            if (September.size > 0){
                values.add(Entry(9f,September.average().toFloat()))
            }
            if (October.size > 0){
                values.add(Entry(10f,October.average().toFloat()))
            }
            if (November.size > 0){
                values.add(Entry(11f,November.average().toFloat()))
            }
            if (December.size > 0){
                values.add(Entry(12f,December.average().toFloat()))
            }
            val dataSet = LineDataSet(values, "Label")
            dataSet.setDrawValues(true)
            dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
            dataSet.cubicIntensity = 0.2f
            dataSet.color = ContextCompat.getColor(requireContext(), R.color.white)
            dataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.white)

            val dataSets = mutableListOf<ILineDataSet>()
            dataSets.add(dataSet)

            val lineData = LineData(dataSets)
            binding.lineChartWeight.data = lineData

            binding.lineChartWeight.description.isEnabled = false
            binding.lineChartWeight.legend.isEnabled = false
            binding.lineChartWeight.setTouchEnabled(true)
            binding.lineChartWeight.isDragEnabled = true
            binding.lineChartWeight.setScaleEnabled(true)
            binding.lineChartWeight.setPinchZoom(true)
            binding.lineChartWeight.axisRight.isEnabled = false
            binding.lineChartWeight.axisRight.setDrawLabels(false)
            binding.lineChartWeight.xAxis.isEnabled = true
            binding.lineChartWeight.xAxis.setDrawLabels(true)
            binding.lineChartWeight.xAxis.position = XAxis.XAxisPosition.BOTTOM
            binding.lineChartWeight.moveViewToX(0f)
            xAxis.setDrawGridLines(false)
            xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)
            xAxis.axisLineColor = ContextCompat.getColor(requireContext(), R.color.transfer)
            xAxis.axisMinimum = 0f
            xAxis.axisMaximum = maxX
            xAxis.granularity = 1f
            yAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)
            yAxis.axisMinimum = 0f
            yAxis.axisMaximum = 400f
            yAxis.axisLineColor =  ContextCompat.getColor(requireContext(), R.color.transfer)
            binding.lineChartWeight.setVisibleXRangeMinimum(4f)
            binding.lineChartWeight.setVisibleXRangeMaximum(4f)
            binding.lineChartWeight.setVisibleYRangeMaximum(200f,YAxis.AxisDependency.LEFT)
            binding.lineChartWeight.invalidate()
        }

    }

    override fun onClickListener() {
        binding.tvYear.text = dialog.year.toString()
        binding.tvYearWeight.text = dialog.year.toString()
        displayChart()
        displayChartWeight()
        activity?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                )
    }

    override fun onHideSystemBar() {
        activity?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                )
    }
}