package com.control.paymentcontrol.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.control.paymentcontrol.R
import com.control.paymentcontrol.databinding.ItemCarrouselBinding
import com.control.paymentcontrol.databinding.ItemCircleBinding
import com.control.paymentcontrol.models.years.CreateNewYear

class AdapterCircle (var count: Int,var select:Int,var context:Context): RecyclerView.Adapter<AdapterCircle.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCircleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position,select,context)
    }

    override fun getItemCount(): Int {
        return count
    }

    class ViewHolder(val binding: ItemCircleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position:Int,select:Int, context:Context){
            binding.imgCircle.setCardBackgroundColor(ContextCompat.getColor(context, if (position == select) R.color.accent else R.color.divider))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSelect(select:Int){
        this.select = select
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSize(count:Int){
        this.count = count
        notifyDataSetChanged()
    }
}