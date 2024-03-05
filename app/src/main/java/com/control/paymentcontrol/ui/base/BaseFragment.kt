package com.control.paymentcontrol.ui.base

import android.app.Notification.Action
import android.content.Context
import android.content.DialogInterface.OnClickListener
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.control.paymentcontrol.R
import com.control.paymentcontrol.models.AttributesDesign
import com.control.paymentcontrol.ui.utils.FormatsMoney
import com.control.paymentcontrol.ui.utils.InterfaceNavBar
import com.control.paymentcontrol.ui.utils.OnActionButtonNavBarMenu
import com.control.paymentcontrol.ui.utils.OnClickInterface
import com.example.awesomedialog.AwesomeDialog
import com.example.awesomedialog.body
import com.example.awesomedialog.icon
import com.example.awesomedialog.onNegative
import com.example.awesomedialog.onPositive
import com.example.awesomedialog.position
import com.example.awesomedialog.title
import com.google.gson.Gson

open class BaseFragment : Fragment() {
    private lateinit var interfaceNavBar: InterfaceNavBar
    protected var gson = Gson()
    protected var format = FormatsMoney()

    protected fun showOrHiddenNavbar(boolean: Boolean){
        if (requireActivity() is InterfaceNavBar) {
            interfaceNavBar =  requireActivity() as InterfaceNavBar
            interfaceNavBar.showOrHiddenNavbar(boolean)
        }
    }

    protected fun showOrHiddenMenuNavbar(boolean: Boolean){
        if (requireActivity() is InterfaceNavBar) {
            interfaceNavBar =  requireActivity() as InterfaceNavBar
            interfaceNavBar.showOrHiddenMenuNavbar(boolean)
        }
    }
    protected fun hideKeyboard(view: View) {
        if (view != null) {
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
    }
    protected fun onClickMoreNavbar(action: OnActionButtonNavBarMenu){
        if (requireActivity() is InterfaceNavBar) {
            interfaceNavBar =  requireActivity() as InterfaceNavBar
            interfaceNavBar.onClickMore(action)
        }
    }

    protected fun getStringRes(resource:Int):String{
        return requireActivity().getString(resource)
    }
    protected fun dialogMessageDefault(title:String = getStringRes(R.string.success),body:String = getStringRes(R.string.body_dialog_message_success),type:Int = 0){
        AwesomeDialog.build(requireActivity())
            .title(title)
            .body(body)
            .icon(
                    if (type == 0) R.drawable.check_circle_solid
                    else if(type == 1)R.drawable.exclamation_triangle_solid
                    else R.drawable.error_solid
            )
            .position(AwesomeDialog.POSITIONS.CENTER)
            .onPositive(getStringRes(R.string.accept), R.drawable.background_button_accept) {}
    }

    protected fun dialogMessageTitle(title:String = getStringRes(R.string.success),type:Int = 0){
        AwesomeDialog.build(requireActivity())
            .title(title)
            .icon(
                if (type == 0) R.drawable.check_circle_solid
                else if(type == 1)R.drawable.exclamation_triangle_solid
                else R.drawable.error_solid
            )
            .position(AwesomeDialog.POSITIONS.CENTER)
            .onPositive(getStringRes(R.string.accept), R.drawable.background_button_accept) {}
    }

    protected fun dialogMessageOnAction(title:String = getStringRes(R.string.success),body:String = getStringRes(R.string.body_dialog_message_success),
            type:Int = 0,titleButton:String = "",listener:OnClickInterface){

        AwesomeDialog.build(requireActivity())
            .title(title)
            .body(body)
            .icon(
                if (type == 0) R.drawable.check_circle_solid
                else if(type == 1)R.drawable.exclamation_triangle_solid
                else R.drawable.error_solid
            )
            .position(AwesomeDialog.POSITIONS.CENTER)
            .onPositive(titleButton,R.drawable.background_button_accept) {
                listener.onClickAction()
            }
    }

}