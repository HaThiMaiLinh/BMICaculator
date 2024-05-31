package com.example.bmicalculator.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bmicalculator.R
import com.example.bmicalculator.databinding.ItemLanguageBinding
import com.example.bmicalculator.model.LanguageModel

class LanguageAdapter(private val context: Context, private var langugelist: ArrayList<LanguageModel>, private val iLangugeItem: ILanguageItem)
    : RecyclerView.Adapter<LanguageAdapter.LangugeViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LangugeViewholder {
        val binding = ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LangugeViewholder(binding)
    }

    override fun getItemCount(): Int {
        return langugelist.size
    }

    fun setCheck(code: String?) {
        for (item in langugelist) {
            item.active = item.code.equals(code)
        }
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: LangugeViewholder, @SuppressLint("RecyclerView") position: Int) {
        langugelist.get(position)?.let { holder.bind(it) }

        if (langugelist.get(position).active){
            holder.itemBinding.imgSelect.setImageResource(R.drawable.ic_select_language)
        }else{
            holder.itemBinding.imgSelect.setImageResource(R.drawable.ic_un_select_language)
        }
        holder.itemBinding.getRoot().setOnClickListener { v ->
            setCheck(langugelist.get(position).code)
            iLangugeItem.onClickItemLanguage(langugelist.get(position).code,position)
            notifyDataSetChanged()
        }

    }
    inner class LangugeViewholder( val itemBinding : ItemLanguageBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(languge : LanguageModel){
            itemBinding.imgflag.setImageResource(languge.img)
            itemBinding.tvLanguge.text = languge.languge
        }
    }

    interface ILanguageItem {
        fun onClickItemLanguage(code: String?,position: Int)
    }

}