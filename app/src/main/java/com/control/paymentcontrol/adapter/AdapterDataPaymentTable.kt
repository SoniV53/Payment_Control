package com.control.paymentcontrol.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.control.paymentcontrol.databinding.ItemListTableBinding
import com.control.paymentcontrol.ui.utils.FormatsMoney
import com.control.roomdatabase.entities.SpentEntity


class AdapterDataPaymentTable (var list: List<SpentEntity>,var context: Context,var listener:OnClickButton): RecyclerView.Adapter<AdapterDataPaymentTable.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListTableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position],context,listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: ItemListTableBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:SpentEntity,context: Context,listener:OnClickButton){
            binding.tbTitle.visibility = if (layoutPosition == 0) View.VISIBLE else View.GONE

            var format = FormatsMoney()
            binding.txtDesItem.text = item.description.takeIf { it.isNotEmpty() } ?: item.title
            binding.txtAmountItem.text = format.formatCurrency(item.amount)
            binding.txtTitleItem.text = item.title.uppercase() + " "

            onActionChecked(item,listener)
        }

        private fun onActionChecked(item:SpentEntity,listener: OnClickButton){
            binding.chCancel.isChecked = item.cancelPay.equals("1")

            binding.chCancel.setOnCheckedChangeListener{bu,che ->
                item.cancelPay =  if (binding.chCancel.isChecked )"1" else "0"
                listener.onEdit(item,che)
            }

            binding.tbContent.setOnClickListener{
                val check = binding.chCancel.isChecked
                binding.chCancel.isChecked = !check
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list:List<SpentEntity>){
        this.list = list
        notifyDataSetChanged()
    }
    interface OnClickButton{
        fun onEdit(item: SpentEntity,check:Boolean)
    }
}