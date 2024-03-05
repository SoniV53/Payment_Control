package com.control.paymentcontrol.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.control.paymentcontrol.R
import com.control.paymentcontrol.databinding.ItemCarrouselBinding
import com.control.paymentcontrol.databinding.ItemMonthBinding
import com.control.paymentcontrol.databinding.ItemPaymentListBinding
import com.control.paymentcontrol.models.years.CreateNewYear
import com.control.paymentcontrol.ui.utils.FormatsMoney
import com.control.roomdatabase.entities.MonthEntity
import com.control.roomdatabase.entities.SpentEntity
import com.control.roomdatabase.entities.YearsEntity
import com.control.roomdatabase.entities.embeddedWith.MonthWithSpent

class AdapterDataPayment (var list: List<SpentEntity>, var context: Context,var listener:OnClickButton ): RecyclerView.Adapter<AdapterDataPayment.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPaymentListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position],context,listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: ItemPaymentListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:SpentEntity,context: Context,listener:OnClickButton){
            var format = FormatsMoney()

            binding.tbrQuotes.visibility = GONE
            binding.tbrQuotesPay.visibility = GONE

            binding.txtDes.text = item.description
            binding.txtAmount.text = format.formatCurrency(item.amount)
            binding.txtTitle.text = item.title.uppercase()

            if (item.numberQuota.isNotEmpty()){
                binding.tbrQuotes.visibility = VISIBLE
                binding.tbrQuotesPay.visibility = VISIBLE
                binding.txtNumQuotes.text = item.numberQuota
                binding.txtAmountQuote.text = format.formatCurrency(calcQuotes(item.amount,item.numberQuota))

                binding.txtNumQuotesPay.text = if (item.quotaPaid.isNotEmpty())item.quotaPaid else "0"
                binding.txtAmountQuotePay.text = format.formatCurrency(calcQuotes(item.amount,item.quotaPaid))
            }

            binding.chCancel.isChecked = item.cancelPay.equals("1")

            binding.dividerStatus.setBackgroundColor(if (item.cancelPay.equals("1")) context.getColor(R.color.accent) else context.getColor(R.color.error))

            binding.chCancel.setOnCheckedChangeListener{bu,che ->
                item.cancelPay =  if (binding.chCancel.isChecked )"1" else "0"
                binding.dividerStatus.setBackgroundColor(if (item.cancelPay.equals("1")) context.getColor(R.color.accent) else context.getColor(R.color.error))
                listener.onEdit(item,che)
            }

            binding.btnEliminar.setOnClickListener{
                listener.onClickDelete(item)
            }

            binding.btnEditar.setOnClickListener{
                listener.onClickDetails(item)
            }

            binding.mainConstra.setOnClickListener{
                val check = binding.chCancel.isChecked
                binding.chCancel.isChecked = !check

                binding.dividerStatus.setBackgroundColor(if (item.cancelPay.equals("1")) context.getColor(R.color.accent) else context.getColor(R.color.error))

            }
        }
        private fun calcQuotes(value:String,quote:String):String{
            if (value.isNotEmpty() && quote.isNotEmpty())
                return (value.toDouble() * quote.toDouble()).toString()
            else return "0"
        }
    }



    interface OnClickButton{
        fun onClickDelete(item: SpentEntity)
        fun onClickDetails(item: SpentEntity)
        fun onEdit(item: SpentEntity,check:Boolean)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list:List<SpentEntity>){
        this.list = list
        notifyDataSetChanged()
    }
}