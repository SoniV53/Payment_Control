package com.control.paymentcontrol.component.constraint

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.control.paymentcontrol.R
import com.control.paymentcontrol.databinding.BackgroundCalendarBinding
import com.control.paymentcontrol.databinding.FragmentHomeBinding
import com.control.paymentcontrol.databinding.ItemCarrouselBinding

class BackgroundCalendar : ConstraintLayout {
    private lateinit var binding: BackgroundCalendarBinding

    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize()
    }

    private fun initialize() {
        binding = BackgroundCalendarBinding.inflate(LayoutInflater.from(context), this, true)

    }

    fun getBindingCalendar():BackgroundCalendarBinding{
       return binding
    }
}