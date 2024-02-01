package com.control.paymentcontrol.ui.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.control.paymentcontrol.R
import com.control.paymentcontrol.databinding.ActivityMainBinding
import com.control.paymentcontrol.ui.utils.InterfaceNavBar
import com.control.paymentcontrol.ui.utils.OnActionButtonNavBar

class MainActivity : AppCompatActivity() , InterfaceNavBar {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun hiddenNavBar() {
        binding.navBar.root.visibility = View.GONE
    }

    override fun showNavbar() {
        binding.navBar.root.visibility = View.VISIBLE
    }

    override fun onClickMore(action: OnActionButtonNavBar) {
        binding.navBar.root.setOnClickListener {
            action.onActionMore()
        }

    }
}