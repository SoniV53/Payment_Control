package com.control.paymentcontrol.component.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.control.paymentcontrol.component.models.ColumnsRowModel
import com.control.paymentcontrol.databinding.ItemListTableBinding
import com.control.paymentcontrol.databinding.ItemMonthBinding
import com.control.paymentcontrol.databinding.ItemTableColumRowBinding
import com.control.paymentcontrol.ui.utils.FormatsMoney
import com.control.roomdatabase.entities.SpentEntity
import java.util.Objects


class AdapterTableData (var list:List<ColumnsRowModel>,var context: Context): RecyclerView.Adapter<AdapterTableData.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTableColumRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding,context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: ItemTableColumRowBinding,val context:Context) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item:ColumnsRowModel){
            binding.cardTitle.visibility = View.VISIBLE
            binding.txtData.visibility = View.GONE
            binding.divider.visibility = View.GONE
            typeTitle(item)
            typeRow(item)
        }

        private fun typeTitle(item:ColumnsRowModel){
            binding.txtTitle.text = item.name + " " + layoutPosition
        }

        private fun typeRow(item:ColumnsRowModel){
            var adapterRow = AdapterTableRowData(item.row)

            binding.recyclerRow.layoutManager = LinearLayoutManager(context)
            binding.recyclerRow.adapter = adapterRow
        }


    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list:List<ColumnsRowModel>){
        this.list = list
        notifyDataSetChanged()
    }
    interface OnClickButton{
        fun onEdit()
    }
}