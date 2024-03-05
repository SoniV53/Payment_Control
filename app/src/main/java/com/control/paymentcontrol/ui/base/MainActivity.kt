package com.control.paymentcontrol.ui.base

import android.annotation.SuppressLint
import android.graphics.drawable.InsetDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import com.control.paymentcontrol.R
import com.control.paymentcontrol.databinding.ActivityMainBinding
import com.control.paymentcontrol.models.AttributesDesign
import com.control.paymentcontrol.ui.utils.InterfaceNavBar
import com.control.paymentcontrol.ui.utils.OnActionButtonNavBarMenu

class MainActivity : AppCompatActivity() , InterfaceNavBar {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun showOrHiddenNavbar(boolean: Boolean) {
        binding.navBar.root.visibility = if (boolean)View.VISIBLE else View.GONE
    }

    override fun showOrHiddenMenuNavbar(boolean: Boolean) {
        binding.navBar.actionMenu.visibility = if (boolean)View.VISIBLE else View.GONE
    }

    override fun onClickMore(action: OnActionButtonNavBarMenu) {
        binding.navBar.actionMenu.setOnClickListener {v ->
            showMenu(v,action)
        }

    }
    @SuppressLint("RestrictedApi", "ObsoleteSdkInt")
    private fun showMenu(v: View,action: OnActionButtonNavBarMenu) {
        //val wrapper: Context = ContextThemeWrapper(this,R.style.popupMenuStyle)
        val showPopUp = PopupMenu(this,v)
        showPopUp.inflate(R.menu.default_menu)

        action.attr.forEach { item ->
            when(item.position){
                1-> getAttributesMenu(showPopUp,R.id.addItemOne,item)
                2-> getAttributesMenu(showPopUp,R.id.addItemTwo,item)
            }
        }

        showPopUp.setOnMenuItemClickListener {item ->
            when(item.itemId){
                   R.id.addItemOne -> {
                       action.onActionPositionOne()
                       true
                   }
                R.id.addItemTwo -> {
                    action.onActionPositionTwo()
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
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Float.MIN_VALUE, resources.displayMetrics)
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

    private fun getAttributesMenu(showPopUp:PopupMenu,id:Int,attr:AttributesDesign){
        showPopUp.menu.findItem(id).setTitle(attr.title)
        showPopUp.menu.findItem(id).setIcon(attr.icon)
        showPopUp.menu.findItem(id).setVisible(attr.visible)
    }

}