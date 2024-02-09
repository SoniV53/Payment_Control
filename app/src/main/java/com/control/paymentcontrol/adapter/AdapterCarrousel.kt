package com.control.paymentcontrol.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.control.paymentcontrol.databinding.ItemCarrouselBinding
import com.control.roomdatabase.entities.YearsEntity

class AdapterCarrousel (var listYear: List<YearsEntity>): RecyclerView.Adapter<AdapterCarrousel.ViewHolder>(){


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
        fun bind(item:YearsEntity){
            binding.txtTitle.text = item.name
            binding.txtSupTitle.text = item.description
            binding.txtCount.text = item.amount
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSelect(listYear:List<YearsEntity>){
        this.listYear = listYear
        notifyDataSetChanged()
    }
}