package com.control.paymentcontrol.component.constraint

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.control.paymentcontrol.R

class BackgroundCalendar : ConstraintLayout {

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
        LayoutInflater.from(context).inflate(R.layout.background_calendar, this, true)
    }
}