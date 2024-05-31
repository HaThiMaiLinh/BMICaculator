package com.example.bmicalculator.ui.fragment

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.bmicalculator.R
import com.example.bmicalculator.ViewModel.BmiViewModel
import com.example.bmicalculator.ViewModel.BmiViewModelFactory
import com.example.bmicalculator.base.Application
import com.example.bmicalculator.base.BaseFragment
import com.example.bmicalculator.databinding.FragmentWeeklyBinding
import com.example.bmicalculator.dialog.MonthDialog
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.util.Calendar


class WeeklyFragment : BaseFragment<FragmentWeeklyBinding>(),MonthDialog.OnMonthClickListener {

    private val viewModel: BmiViewModel by viewModels {
        BmiViewModelFactory((requireActivity().application as Application).repository)
    }
    private val dialog: MonthDialog by lazy {
        MonthDialog(requireContext(),this)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWeeklyBinding {
        return FragmentWeeklyBinding.inflate(layoutInflater)
    }

    override fun initView() {
        binding.tvMonth.text = getCurrentMonth()
        initListener()
        displayChart()
        displayChartWeight()
    }

    fun getCurrentMonth(): String {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) + 1
        return when (month) {
            1 ->  requireContext().getString(R.string.January)
            2 -> requireContext().getString(R.string.February)
            3 -> requireContext().getString(R.string.March)
            4-> requireContext().getString(R.string.April)
            5 -> requireContext().getString(R.string.May)
            6 -> requireContext().getString(R.string.June)
            7 -> requireContext().getString(R.string.July)
            8 -> requireContext().getString(R.string.August)
            9 -> requireContext().getString(R.string.September)
            10 -> requireContext().getString(R.string.October)
            11 -> requireContext().getString(R.string.November)
            12 -> requireContext().getString(R.string.December)
            else -> {
                return ""
            }
        }
    }

    private fun initListener(){
        var isClickfilter = false
        binding.filterChart.setOnClickListener {
            if (!isClickfilter){
                isClickfilter = true
                dialog.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    isClickfilter =false
                },500)
            }
        }
    }



    private fun displayChart() {
        val values = mutableListOf<Entry>()
        val weekly_1 = mutableListOf<Float>()
        val weekly_2 = mutableListOf<Float>()
        val weekly_3 = mutableListOf<Float>()
        val weekly_4 = mutableListOf<Float>()
        val xAxis: XAxis = binding.lineChartBmi.xAxis
        val yAxis: YAxis = binding.lineChartBmi.axisLeft
        val maxX = 4f
        viewModel.allBmi.observe( this){listbmi->
            for (it in listbmi){

                val time = it.time.substringAfter(".")
                val month = time.substringBefore(".").toInt()
                var monthIndex = 1

                when(binding.tvMonth.text){
                    context?.getString(R.string.January)->monthIndex = 1
                    context?.getString(R.string.February)-> monthIndex = 2
                    context?.getString(R.string.March)-> monthIndex = 3
                    context?.getString(R.string.April)->monthIndex = 4
                    context?.getString(R.string.May)-> monthIndex = 5
                    context?.getString(R.string.June)-> monthIndex = 6
                    context?.getString(R.string.July)->monthIndex = 7
                    context?.getString(R.string.August)->monthIndex = 8
                    context?.getString(R.string.September)->monthIndex = 9
                    context?.getString(R.string.October)->monthIndex = 10
                    context?.getString(R.string.November)->monthIndex = 11
                    context?.getString(R.string.December)->monthIndex = 12
                }
                if (month == monthIndex){

                    val x = it.time.substringBefore(".").toFloat()
                    val y = it.bmi.toFloat()
                    values.add(Entry(x, y))
                    val groupedByX = values.groupBy { it.x }
                    val result = groupedByX.map { (x, entriesWithSameX) ->
                        val averageY = entriesWithSameX.map { it.y }.average()
                        Entry(x, averageY.toFloat())
                    }
                    values.clear()

                    result.forEach { entryValue ->
                        Log.d("ka",entryValue.x.toString())
                        if (entryValue.x >= 1 && entryValue.x < 8){
                            weekly_1.add(entryValue.y)
                        }else if (entryValue.x >= 8 && entryValue.x <16){
                            weekly_2.add(entryValue.y)
                        }else if (entryValue.x >= 16 && entryValue.x < 23){
                            weekly_3.add(entryValue.y)
                        }else if (entryValue.x >=23){
                            weekly_4.add(entryValue.y)
                        }
                    }
                }
            }

            if (weekly_1.size >0){
                values.add(Entry(1f, weekly_1.average().toFloat()))
            }
            if (weekly_2.size >0){
                values.add(Entry(2f, weekly_2.average().toFloat()))
            }
            if (weekly_3.size >0){
                values.add(Entry(3f, weekly_3.average().toFloat()))
            }
            if (weekly_4.size >0){
                values.add(Entry(4f, weekly_4.average().toFloat()))
            }
            val dataSet = LineDataSet(values, "Label")
            dataSet.setDrawValues(true) // Hiển thị giá trị trên điểm
            dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER // Sử dụng đường cong Bézier
            dataSet.cubicIntensity = 0.2f // Điều chỉnh độ cong của đường
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
            xAxis.textColor = ContextCompat.getColor(requireContext(),R.color.white)
            xAxis.axisLineColor = ContextCompat.getColor(requireContext(),R.color.transfer)
            xAxis.axisMinimum = 0f
            xAxis.axisMaximum = maxX
            xAxis.granularity = 1f
            yAxis.textColor = ContextCompat.getColor(requireContext(),R.color.white)
            yAxis.axisMinimum = 1f
            yAxis.axisMaximum = 80f
            yAxis.axisLineColor =  ContextCompat.getColor(requireContext(),R.color.transfer)
            binding.lineChartBmi.setVisibleXRangeMaximum(4f)
            binding.lineChartBmi.setVisibleYRangeMaximum(80f,YAxis.AxisDependency.LEFT)
            binding.lineChartBmi.invalidate()
        }
    }

    private fun displayChartWeight() {
        val values = mutableListOf<Entry>()
        val weekly_1 = mutableListOf<Float>()
        val weekly_2 = mutableListOf<Float>()
        val weekly_3 = mutableListOf<Float>()
        val weekly_4 = mutableListOf<Float>()
        val xAxis: XAxis = binding.lineChartWeight.xAxis
        val yAxis: YAxis = binding.lineChartWeight.axisLeft
        val maxX = 4f
        viewModel.allBmi.observe( this){listbmi->
            for (it in listbmi){

                val time = it.time.substringAfter(".")
                val month = time.substringBefore(".").toInt()
                var monthIndex = 1

                when(binding.tvMonth.text){
                    context?.getString(R.string.January)->monthIndex = 1
                    context?.getString(R.string.February)-> monthIndex = 2
                    context?.getString(R.string.March)-> monthIndex = 3
                    context?.getString(R.string.April)->monthIndex = 4
                    context?.getString(R.string.May)-> monthIndex = 5
                    context?.getString(R.string.June)-> monthIndex = 6
                    context?.getString(R.string.July)->monthIndex = 7
                    context?.getString(R.string.August)->monthIndex = 8
                    context?.getString(R.string.September)->monthIndex = 9
                    context?.getString(R.string.October)->monthIndex = 10
                    context?.getString(R.string.November)->monthIndex = 11
                    context?.getString(R.string.December)->monthIndex = 12
                }
                if (month == monthIndex){
                    val weight = it.weight.substringBefore(" ")
                    val x = it.time.substringBefore(".").toFloat()
                    val y = weight.toFloat()
                    values.add(Entry(x, y))
                    val groupedByX = values.groupBy { it.x }
                    val result = groupedByX.map { (x, entriesWithSameX) ->
                        val averageY = entriesWithSameX.map { it.y }.average()
                        Entry(x, averageY.toFloat())
                    }
                    values.clear()

                    result.forEach { entryValue ->
                        if (entryValue.x >= 1 && entryValue.x < 8){
                            weekly_1.add(entryValue.y)
                        }else if (entryValue.x >= 8 && entryValue.x <16){
                            weekly_2.add(entryValue.y)
                        }else if (entryValue.x >= 16 && entryValue.x < 23){
                            weekly_3.add(entryValue.y)
                        }else if (entryValue.x >=23){
                            weekly_4.add(entryValue.y)
                        }
                    }
                }
            }
            if (weekly_1.size >0){
                values.add(Entry(1f, weekly_1.average().toFloat()))
            }
            if (weekly_2.size >0){
                values.add(Entry(2f, weekly_2.average().toFloat()))
            }
            if (weekly_3.size >0){
                values.add(Entry(3f, weekly_3.average().toFloat()))
            }
            if (weekly_4.size >0){
                values.add(Entry(4f, weekly_4.average().toFloat()))
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
            yAxis.axisMinimum = 1f
            yAxis.axisMaximum = 400f
            yAxis.axisLineColor =  ContextCompat.getColor(requireContext(), R.color.transfer)
            binding.lineChartWeight.setVisibleXRangeMaximum(4f)
            binding.lineChartWeight.setVisibleYRangeMaximum(200f,YAxis.AxisDependency.LEFT)
            binding.lineChartWeight.invalidate()
        }
    }

    override fun onClickListener() {
        binding.tvMonth.text = dialog.monthitem
        binding.tvMonthWeight.text = dialog.monthitem
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