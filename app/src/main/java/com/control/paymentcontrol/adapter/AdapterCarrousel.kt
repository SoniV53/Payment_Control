package com.control.paymentcontrol.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.control.paymentcontrol.databinding.ItemCarrouselBinding
import com.control.roomdatabase.entities.YearsEntity

class AdapterCarrousel (var listYear: List<YearsEntity>,var onActionClick:OnClickButton): RecyclerView.Adapter<AdapterCarrousel.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCarrouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listYear[position],onActionClick)
    }

    override fun getItemCount(): Int {
        return listYear.size
    }

    class ViewHolder(val binding: ItemCarrouselBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:YearsEntity,onActionClick:OnClickButton){
            binding.txtTitle.text = item.name
            binding.btnDelete.setOnClickListener {
                onActionClick?.onClickDelete(item,layoutPosition)
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSelect(listYear:List<YearsEntity>,position:Int){
        notifyItemRemoved(position)
        this.listYear = listYear
        notifyDataSetChanged()
    }

    interface OnClickButton{
        fun onClickDelete(item:YearsEntity,position: Int)
    }
}