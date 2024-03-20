package com.control.paymentcontrol.component.constraint

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.control.paymentcontrol.R
import com.google.android.material.card.MaterialCardView

class IconDate : ConstraintLayout {

    private lateinit var view:View
    private lateinit var txtTitle:TextView
    private lateinit var cardLateral:MaterialCardView
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
        view = LayoutInflater.from(context).inflate(R.layout.icon_item_year, this, true)
        txtTitle = findViewById(R.id.txtTitle)
        cardLateral = findViewById(R.id.cardLateral)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconItemAttr)

        txtTitle.text = typedArray.getString(R.styleable.IconItemAttr_title)
        cardLateral.visibility = if (typedArray.getString(R.styleable.IconItemAttr_visible).equals("gone"))View.GONE else VISIBLE

        typedArray.recycle()
    }

    fun setTextTitle(text:String){
        txtTitle = findViewById(R.id.txtTitle)
        txtTitle.text = text
    }
}