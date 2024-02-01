package com.control.paymentcontrol.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.control.paymentcontrol.databinding.ItemCarrouselBinding
import com.control.paymentcontrol.models.years.CreateNewYear

class AdapterCarrousel (var listYear: List<CreateNewYear>,): RecyclerView.Adapter<AdapterCarrousel.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCarrouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listYear[position])
    }

    override fun getItemCount(): Int {
        return listYear.size
    }

    class ViewHolder(val binding: ItemCarrouselBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:CreateNewYear){
            binding.txtTitle.text = item.nameYear
            binding.txtSupTitle.text = item.months
            binding.txtCount.text = item.total
        }
    }
}