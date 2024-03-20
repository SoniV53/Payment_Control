package com.control.paymentcontrol.component.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.control.paymentcontrol.component.models.ColumnsRowModel
import com.control.paymentcontrol.component.models.RowTableModel
import com.control.paymentcontrol.databinding.ItemListTableBinding
import com.control.paymentcontrol.databinding.ItemMonthBinding
import com.control.paymentcontrol.databinding.ItemTableColumRowBinding
import com.control.paymentcontrol.databinding.ItemTableRowBinding
import com.control.paymentcontrol.ui.utils.FormatsMoney
import com.control.roomdatabase.entities.SpentEntity
import java.util.Objects


class AdapterTableRowData (var list:List<RowTableModel>): RecyclerView.Adapter<AdapterTableRowData.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTableRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: ItemTableRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:RowTableModel){
            typeRow(item)
        }

        private fun typeRow(item:RowTableModel){
            binding.txtData.text = item.name
        }



    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list:List<RowTableModel>){
        this.list = list
        notifyDataSetChanged()
    }
    interface OnClickButton{
        fun onEdit()
    }
}