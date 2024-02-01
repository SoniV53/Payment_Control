package com.control.paymentcontrol.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.control.paymentcontrol.R
import com.control.paymentcontrol.databinding.ItemCarrouselBinding
import com.control.paymentcontrol.databinding.ItemMonthBinding
import com.control.paymentcontrol.models.years.CreateNewYear

class AdapterDataMonth (var listYear: List<CreateNewYear>): RecyclerView.Adapter<AdapterDataMonth.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listYear[position])
    }

    override fun getItemCount(): Int {
        return listYear.size
    }

    class ViewHolder(val binding: ItemMonthBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:CreateNewYear){
            binding.txtTitle.text = item.nameYear
            binding.txtSupTitle.text = item.months
            binding.txtCount.text = item.total

            binding.cardMain.alpha = if (item.nameYear.trim().isEmpty()) 0f else 1f
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(listYear:List<CreateNewYear>){
        this.listYear = listYear
        notifyDataSetChanged()
    }
}