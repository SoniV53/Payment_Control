package com.control.paymentcontrol.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.control.paymentcontrol.R
import com.control.paymentcontrol.adapter.AdapterCarrousel
import com.control.paymentcontrol.adapter.AdapterCircle
import com.control.paymentcontrol.adapter.AdapterDataMonth
import com.control.paymentcontrol.databinding.FragmentHomeBinding
import com.control.paymentcontrol.models.AttributesDesign
import com.control.paymentcontrol.ui.base.BaseFragment
import com.control.paymentcontrol.ui.utils.OnActionButtonNavBarMenu
import com.control.paymentcontrol.ui.utils.OnClickInterface
import com.control.paymentcontrol.ui.utils.PutArgumentsString
import com.control.paymentcontrol.ui.utils.PutArgumentsString.MONTH_SELECT
import com.control.paymentcontrol.ui.utils.PutArgumentsString.YEAR_SELECT
import com.control.paymentcontrol.viewmodels.ServicePaymentViewModel
import com.control.roomdatabase.entities.MonthEntity
import com.control.roomdatabase.entities.SpentEntity
import com.control.roomdatabase.entities.YearsEntity
import com.control.roomdatabase.repository.ui.YearItemRepository
import com.control.roomdatabase.utils.Status
import com.control.roomdatabase.utils.Status.SUCCESS
import com.example.awesomedialog.AwesomeDialog
import com.example.awesomedialog.body
import com.example.awesomedialog.onNegative
import com.example.awesomedialog.onPositive
import com.example.awesomedialog.title
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import java.util.Arrays
import java.util.Calendar
import java.util.Objects

class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    private var selectPosition: Int = 0
    private lateinit var listYear: List<YearsEntity>
    private lateinit var adapterCarrousel: AdapterCarrousel
    private lateinit var adapterCircle: AdapterCircle
    private lateinit var adapterMonth: AdapterDataMonth
    private lateinit var viewModel: ServicePaymentViewModel
    private var positionCarrousel: Int = 0
    private var yearItem:YearsEntity ?= null
    private lateinit var listMonth: List<MonthEntity>
    private lateinit var listMonthOriginal: List<MonthEntity>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ServicePaymentViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        showOrHiddenMenuNavbar(true)
        yearItem = getPreferenceGson()


        onClickMoreNavbar(object:OnActionButtonNavBarMenu{
            override fun onActionPositionTwo() {
                findNavController().navigate(R.id.action_homeFragment_to_addYearFragment)
            }

            override fun onActionPositionOne() {
                val bundle = Bundle()
                bundle.putString(PutArgumentsString.ID_MONTH,"")
                findNavController().navigate(R.id.action_homeFragment_to_paymentFavoritesFragment,bundle)
            }

            override var attr: List<AttributesDesign>
                get() = Arrays.asList(AttributesDesign(1,getStringRes(R.string.favorites),R.drawable.star_solid),
                    AttributesDesign(2,getStringRes(R.string.menu_add_year),R.drawable.plus_circle_solid))
                set(value) {}
        })

        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addYearFragment)
        }
        getDataInit()

        return binding.root
    }

    private fun getDataInit(){
        if (!isPreferenceData()){
            binding.iconDate.setTextTitle(yearItem?.name.toString())
            getOrderListMonth()
        }

        binding.constrainEmpty.visibility = if (isPreferenceData()) View.VISIBLE else View.GONE
        binding.mainConstra.visibility = if (!isPreferenceData()) View.VISIBLE else View.GONE
    }

    private fun getOrderListMonth(){
        viewModel.fullByMonths(requireActivity(),yearItem?.id.toString()).observe(requireActivity()) {responseBase ->
            if (responseBase != null) {
                val result = mutableListOf<MonthEntity>()
                result.addAll(responseBase)
                result.add(MonthEntity("",""))
                listMonth = result
                listMonthOriginal = responseBase

            }

        }
    }

    private fun getOrderNewListMonth(){
        var items = requireActivity().resources.getStringArray(R.array.month)
        var list = viewModel.getValidExistMonth(items,listMonth)

        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(resources.getString(R.string.select_month))
            .setItems(list) { _, which ->
                setAddMonth(list[which.inc() - 1],yearItem?.id.toString())
            }
            .show()
    }

    /**
     * ADD NEW Month CONTROLLER
     */
    private fun setAddMonth(nameMonth:String,idYear:String){
        viewModel.setAddMonthDataBase(requireActivity(), MonthEntity(nameMonth,idYear))
        viewModel.getAddMonthDataBase().observe(requireActivity()) {responseBase ->
            if (responseBase.status == SUCCESS){
                dialogMessageTitle(getStringRes(R.string.body_dialog_message_success))
                getOrderListMonth()
            }else{
                dialogMessageDefault(getStringRes(R.string.error),
                    responseBase.message,
                    1
                )
            }
        }
    }


}