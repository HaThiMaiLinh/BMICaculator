package com.example.bmicalculator.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bmicalculator.R
import com.example.bmicalculator.databinding.ItemFilterBinding
import com.example.bmicalculator.model.YearModel

class FilterYearAdapter(private val context: Context, private var yearList: ArrayList<YearModel>, private val listener: IFilterYearItem) :
    RecyclerView.Adapter<FilterYearAdapter.FilterYearViewholder>() {

    inner class FilterYearViewholder( val itemBinding : ItemFilterBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(year : YearModel){
            itemBinding.tvMonth.text = year.year.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterYearViewholder {
        val binding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterYearViewholder(binding)
    }

    override fun getItemCount(): Int {
        return yearList.size
    }

    override fun onBindViewHolder(holder: FilterYearViewholder, position: Int) {
        holder.bind(yearList.get(position))

        if (yearList.get(position).active){
            holder.itemBinding.imgSelect.setImageResource(R.drawable.ic_select_language)
        }else{
            holder.itemBinding.imgSelect.setImageResource(R.drawable.ic_un_select_language)
        }
        holder.itemBinding.root.setOnClickListener { v ->
            setCheck(yearList.get(position).year)
            listener.onClickItemFilter(yearList.get(position).year)
            notifyDataSetChanged()
        }
    }

    fun setCheck(year : Int?) {
        for (item in yearList) {
            item.active = item.year.equals(year)
        }
        notifyDataSetChanged()
    }

    interface IFilterYearItem {
        fun onClickItemFilter(year: Int?)
    }

}