package com.example.bmicalculator.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bmicalculator.Constance
import com.example.bmicalculator.databinding.ItemViewpagerBinding
import com.example.bmicalculator.model.TutorialModel
import com.example.bmicalculator.utils.SharePreferencesUtils

class TutorialAdapter(private val context: Context, private val tutoriallist : List<TutorialModel>): RecyclerView.Adapter<TutorialAdapter.TutorialViewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorialViewholder {
        val binding = ItemViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TutorialViewholder(binding)
    }

    override fun getItemCount(): Int {
        return tutoriallist.size
    }

    override fun onBindViewHolder(holder: TutorialViewholder, position: Int) {
        holder.bind(tutoriallist.get(position))
        if (position != 0){
            holder.binding.tvWelcomeTutorial.visibility = View.GONE
        }
    }
    inner  class TutorialViewholder(val binding : ItemViewpagerBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(tutorial: TutorialModel){
            binding.imageView.setImageResource(tutorial.img)
            binding.tvTitleTutorial.text = context.getString(tutorial.title)
            binding.tvDesTutorial.text = context.getString(tutorial.des)
        }
    }
}