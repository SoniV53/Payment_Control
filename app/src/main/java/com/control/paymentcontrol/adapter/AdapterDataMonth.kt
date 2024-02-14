package com.control.paymentcontrol.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.control.paymentcontrol.R
import com.control.paymentcontrol.databinding.ItemCarrouselBinding
import com.control.paymentcontrol.databinding.ItemMonthBinding
import com.control.paymentcontrol.models.years.CreateNewYear
import com.control.roomdatabase.entities.MonthEntity

class AdapterDataMonth (var listYear: List<MonthEntity>): RecyclerView.Adapter<AdapterDataMonth.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if ((listYear.size + 1) == (position + 1)){
            holder.binding.cardMain.visibility = GONE
            holder.binding.contentDiv.visibility = VISIBLE
        }else{
            holder.bind(listYear[position])
        }

    }

    override fun getItemCount(): Int {
        return listYear.size + 1
    }

    class ViewHolder(val binding: ItemMonthBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:MonthEntity){
            binding.txtTitle.text = item.name
            binding.txtSupTitle.text = item.name
            binding.txtCount.text = item.total

            //binding.cardMain.alpha = if (item.nameYear.trim().isEmpty()) 0f else 1f
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(listYear:List<MonthEntity>){
        this.listYear = listYear
        notifyDataSetChanged()
    }
}