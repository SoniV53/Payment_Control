package com.control.paymentcontrol.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.control.paymentcontrol.R.*
import com.control.paymentcontrol.databinding.ItemListTableBinding
import com.control.paymentcontrol.models.SpentAdapterModel
import com.control.paymentcontrol.ui.utils.FormatsMoney
import com.control.roomdatabase.entities.SpentEntity
import com.zerobranch.layout.SwipeLayout


class AdapterDataPaymentTable (var list: List<SpentAdapterModel>,var context: Context,var listener:OnClickButton): RecyclerView.Adapter<AdapterDataPaymentTable.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListTableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position],context,listener,(list.size - 1))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: ItemListTableBinding) : RecyclerView.ViewHolder(binding.root) {
        private var isMore = false
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(item:SpentAdapterModel, context: Context, listener:OnClickButton,last:Int){
            var format = FormatsMoney()
            when(item.type){
                "m"->{
                    binding.gridTitle.visibility = View.GONE
                    binding.cardMain.visibility = View.VISIBLE
                    binding.tbrQuotes.visibility = View.GONE

                    onActionChecked(item,format,listener)

                    binding.txtTipe.text = if (item.spentEntity.numberQuota.isEmpty())"Normal" else "Cuota"
                    binding.txtName.text = item.spentEntity.title
                    binding.txtDescription.text = item.spentEntity.description.ifEmpty { item.spentEntity.title }
                    binding.txtAmount.text = format.formatCurrency(item.spentEntity.amount)

                    binding.constraintData.background =if (layoutPosition %2 == 0) context.getDrawable(drawable.background_first_table)
                        else context.getDrawable(drawable.background_second_table)

                    var colorS = if (layoutPosition %2 == 0) context.getColor(color.font_button) else context.getColor(color.font_card)
                    binding.gridData.setBackgroundColor(colorS)

                    binding.includeCheck.checked.setCardBackgroundColor(if (item.spentEntity.cancelPay.equals("1"))
                        context.getColor(color.accent)  else colorS)

                    binding.includeCheck.checked.setStrokeColor(if (item.spentEntity.cancelPay.equals("1"))context.getColor(color.accent)
                        else context.getColor(color.primary_text) )

                }
                 else -> {
                     binding.gridTitle.visibility = View.VISIBLE
                     binding.cardMain.visibility = View.GONE
                 }
            }

        }

        private fun viewQuotes(format:FormatsMoney,item:SpentAdapterModel){
            if (item.spentEntity.numberQuota.isNotEmpty() && !isMore){
                binding.tbrQuotes.visibility = View.VISIBLE

                binding.txtNumQuotesPay.text = item.spentEntity.numberQuota
                binding.txtAmountQuotePay.text = format.formatCurrency(calcQuotes(item.spentEntity.amount,item.spentEntity.numberQuota))

                binding.txtNumQuotes.text = if (item.spentEntity.quotaPaid.isNotEmpty())item.spentEntity.quotaPaid else "0"
                binding.txtAmountQuote.text = format.formatCurrency(calcQuotes(item.spentEntity.amount,item.spentEntity.quotaPaid))

                binding.txtAmountQuoteRest.text = format.formatCurrency(calcRes(item))
                isMore = true
            }else{
                binding.tbrQuotes.visibility = View.GONE
                isMore = false
            }
        }

        private fun calcQuotes(value:String,quote:String):String{
            if (value.isNotEmpty() && quote.isNotEmpty())
                return (value.toDouble() * quote.toDouble()).toString()
            else return "0"
        }

        private fun calcRes(item:SpentAdapterModel):String{
            if (item.spentEntity.numberQuota.isNotEmpty() && item.spentEntity.quotaPaid.isNotEmpty()){
                val totalQuotes = item.spentEntity.amount.toDouble() * item.spentEntity.numberQuota.toDouble()
                val totalQuotesPay = item.spentEntity.amount.toDouble() * item.spentEntity.quotaPaid.toDouble()
                return (totalQuotes - totalQuotesPay).toString()
            }
            else return "0"
        }
        private fun onActionChecked(item:SpentAdapterModel,format:FormatsMoney,listener:OnClickButton){
            binding.gridData.setOnClickListener{
                viewQuotes(format,item)
            }
            binding.tbrQuotes.setOnClickListener{
                viewQuotes(format,item)
            }
            binding.constraintData.setOnClickListener{
                viewQuotes(format,item)
            }

            binding.rightView.setOnClickListener{
                listener.onDelete(item.spentEntity)
            }
            binding.leftView.setOnClickListener{
                listener.onEdit(item.spentEntity)
            }

            /*binding.swipeLayout.setOnActionsListener(object :SwipeLayout.SwipeActionsListener{
                override fun onOpen(direction: Int, isContinuous: Boolean) {

                    if (direction == SwipeLayout.RIGHT) {

                    } else if (direction == SwipeLayout.LEFT) {
                        println("PRUEBA OPEN:")
                    }
                }

                override fun onClose() {
                    println("PRUEBA CLOSE:")
                }
            })*/
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list:List<SpentAdapterModel>){
        this.list = list
        notifyDataSetChanged()
    }
    interface OnClickButton{
        fun onEdit(item: SpentEntity)
        fun onDelete(item: SpentEntity)
    }
}