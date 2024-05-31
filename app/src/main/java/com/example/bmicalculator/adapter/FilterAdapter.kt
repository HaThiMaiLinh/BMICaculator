package com.example.bmicalculator.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.bmicalculator.R
import com.example.bmicalculator.databinding.ItemFilterBinding
import com.example.bmicalculator.model.MothModel

class FilterAdapter(private val context: Context,private var monthlist: ArrayList<MothModel>,private val listener: IFilterItem) : Adapter<FilterAdapter.FilterViewholder>() {

    inner class FilterViewholder( val itemBinding : ItemFilterBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(monthModel: MothModel){
            itemBinding.tvMonth.text = context.getString(monthModel.month)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewholder {
        val binding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterViewholder(binding)
    }

    override fun getItemCount(): Int {
        return monthlist.size
    }

    override fun onBindViewHolder(holder: FilterViewholder, position: Int) {
        holder.bind(monthlist.get(position))

        if (monthlist.get(position).active){
            holder.itemBinding.imgSelect.setImageResource(R.drawable.ic_select_language)
        }else{
            holder.itemBinding.imgSelect.setImageResource(R.drawable.ic_un_select_language)
        }
        holder.itemBinding.root.setOnClickListener { v ->
            setCheck(context.getString(monthlist.get(position).month))
            listener.onClickItemFilter(context.getString(monthlist.get(position).month))
            notifyDataSetChanged()
        }
    }

    fun setCheck(month: String?) {
        for (item in monthlist) {
            item.active = context.getString(item.month).equals(month)
        }
        notifyDataSetChanged()
    }

    interface IFilterItem {
        fun onClickItemFilter(month: String?)
    }

}