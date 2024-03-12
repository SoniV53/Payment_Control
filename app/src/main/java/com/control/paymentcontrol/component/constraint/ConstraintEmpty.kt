package com.control.paymentcontrol.component.constraint

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.control.paymentcontrol.R

class ConstraintEmpty : ConstraintLayout {

    private lateinit var view:View
    private lateinit var txtTitle:TextView
    constructor(context: Context) : super(context) {
        initialize(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        initialize(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(attrs)
    }

    @SuppressLint("CustomViewStyleable")
    private fun initialize(attrs: AttributeSet?) {
        view = LayoutInflater.from(context).inflate(R.layout.constraint_empty, this, true)
        txtTitle = findViewById(R.id.txtTitle)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconItemAttr)



        typedArray.recycle()
    }

}