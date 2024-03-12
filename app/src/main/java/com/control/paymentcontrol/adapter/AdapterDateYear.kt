package com.control.paymentcontrol.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.control.paymentcontrol.R
import com.control.paymentcontrol.databinding.ItemLayoutYearBinding
import com.control.paymentcontrol.models.years.YearDataModel

class AdapterDateYear (var list: List<YearDataModel>,
                       var context: Context,
                       var listener:OnClickButton): RecyclerView.Adapter<AdapterDateYear.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutYearBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(list[position],context,listener)
            }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: ItemLayoutYearBinding) : RecyclerView.ViewHolder(binding.root) {
        private var isLongPress = false
        @SuppressLint("ClickableViewAccessibility")
        fun bind(item:YearDataModel,context: Context,listener:OnClickButton){
            when(item.type){
                0 -> bindYear(item,context)
                1 -> bindNew()
            }

            binding.root.setOnLongClickListener {
                listener.onClickDelete(item)
                true
            }

            binding.root.setOnClickListener() {
                listener.onClickDetails(item)
            }
        }

        fun bindYear(item:YearDataModel, context: Context){
            binding.cardYear.visibility = VISIBLE
            binding.cardNew.visibility = GONE

            binding.itemYear.setTextTitle(item.year?.name.toString())

        }
        fun bindNew(){
            binding.cardYear.visibility = GONE
            binding.cardNew.visibility = VISIBLE
        }
    }

    interface OnClickButton{
        fun onClickDetails(item: YearDataModel)
        fun onClickDelete(item: YearDataModel)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list:List<YearDataModel>){
        this.list = list
        notifyDataSetChanged()
    }
}