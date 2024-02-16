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
import com.control.paymentcontrol.models.years.CreateNewYear
import com.control.roomdatabase.entities.MonthEntity

class AdapterDataMonth (var listYear: List<MonthEntity>,var context: Context): RecyclerView.Adapter<AdapterDataMonth.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listYear[position],context )
    }

    override fun getItemCount(): Int {
        return listYear.size
    }

    class ViewHolder(val binding: ItemMonthBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:MonthEntity,context: Context){
            binding.txtTitle.text = item.name
            binding.txtSupTitle.text = item.name
            binding.txtCount.text = item.total

            binding.frameMain.visibility = if (item.name?.isNotEmpty()!!) VISIBLE else GONE
            binding.contentDiv.visibility = if (item.name?.isNotEmpty()!!) GONE else VISIBLE

            binding.actionMenu.setOnClickListener {v ->
                showMenu(v,context)
            }
        }

        @SuppressLint("RestrictedApi", "ObsoleteSdkInt")
        private fun showMenu(v: View,context: Context) {
            //val wrapper: Context = ContextThemeWrapper(this,R.style.popupMenuStyle)
            val showPopUp = PopupMenu(context,v)
            showPopUp.inflate(R.menu.add_delete_menu)
            showPopUp.setOnMenuItemClickListener {item ->
                when(item.itemId){
                    R.id.addYear -> {

                        true
                    }
                    R.id.new_game -> {

                        true
                    }
                    R.id.new_game -> {

                        true
                    }
                }

                false
            }
            if (showPopUp.menu is MenuBuilder) {
                val menuBuilder = showPopUp.menu as MenuBuilder
                menuBuilder.setOptionalIconsVisible(true)
                for (item in menuBuilder.visibleItems) {
                    val iconMarginPx =
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Float.MIN_VALUE,  context.resources.displayMetrics)
                            .toInt()
                    if (item.icon != null) {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                            item.icon = InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0)
                        } else {
                            item.icon =
                                object : InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0) {
                                    override fun getIntrinsicWidth(): Int {
                                        return intrinsicHeight + iconMarginPx + iconMarginPx
                                    }
                                }
                        }
                    }
                }
            }
            showPopUp.show()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(listYear:List<MonthEntity>){
        this.listYear = listYear
        notifyDataSetChanged()
    }
}