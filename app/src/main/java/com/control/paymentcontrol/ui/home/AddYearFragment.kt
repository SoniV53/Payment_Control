package com.control.paymentcontrol.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.control.paymentcontrol.R
import com.control.paymentcontrol.adapter.AdapterDateYear
import com.control.paymentcontrol.databinding.FragmentAddYearBinding
import com.control.paymentcontrol.models.years.YearDataModel
import com.control.paymentcontrol.ui.base.BaseFragment
import com.control.paymentcontrol.ui.utils.OnClickInterface
import com.control.paymentcontrol.viewmodels.ServicePaymentViewModel
import com.control.paymentcontrol.viewmodels.ServiceYearViewModel
import com.control.roomdatabase.entities.YearsEntity
import com.control.roomdatabase.utils.Status
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson


class AddYearFragment : BaseFragment() {
    private lateinit var binding: FragmentAddYearBinding
    private lateinit var adapter: AdapterDateYear
    private lateinit var viewModelYear: ServiceYearViewModel
    private lateinit var viewModel: ServicePaymentViewModel
    private lateinit var listYear: List<YearsEntity>
    private var yearItem:YearsEntity ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelYear = ViewModelProvider(requireActivity())[ServiceYearViewModel::class.java]
        viewModel = ViewModelProvider(requireActivity())[ServicePaymentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddYearBinding.inflate(inflater,container,false)
        showOrHiddenMenuNavbar(false)
        yearItem = getPreferenceGson()

        getOrderListYear()
        return binding.root
    }


    private fun recyclerViewData(){
        val items = viewModel.getYearList(requireActivity())
        adapter = AdapterDateYear(viewModelYear.getListYearFilter(listYear, items.isNotEmpty()),requireActivity(),object: AdapterDateYear.OnClickButton{
            override fun onClickDetails(item: YearDataModel) {
                when(item.type){
                    0 ->{
                        addPreferenceCache(Gson().toJson(item.year))
                        requireActivity().onBackPressed()
                    }
                    1->{
                        val items = viewModel.getYearList(requireActivity())

                        MaterialAlertDialogBuilder(requireActivity())
                            .setTitle(resources.getString(R.string.select_year))
                            .setItems(items) { _, which ->
                                setAddYear(items[which.inc() - 1])
                            }
                            .show()

                    }
                }
            }

            override fun onClickDelete(item: YearDataModel) {
                when(item.type){
                    0 ->{
                        dialogMessageOnAction(getStringRes(R.string.delete),getStringRes(R.string.delete_message),1,getStringRes(R.string.delete),object:
                            OnClickInterface {
                            override fun onClickAction() {
                                if (yearItem?.id == item.year.id)
                                    addPreferenceCache("")
                                setDeleteYear(item.year)
                            }

                        })
                    }

                }

            }

        })
        val layoutManager = GridLayoutManager(requireActivity(),3)
        binding.recyclerData.layoutManager = layoutManager
        binding.recyclerData.adapter = adapter

    }

    //Services
    private fun getOrderListYear(){
        viewModel.fullByYears(requireActivity()).observe(requireActivity()) {responseBase ->
            if (responseBase != null) {
                listYear = responseBase
                recyclerViewData()
            }
        }
    }

    /**
     * ADD NEW YEAR CONTROLLER
     */
    private fun setAddYear(yearItem:String){
        viewModel.setAddYearDataBase(requireActivity(), YearsEntity(yearItem))
        viewModel.getAddYearDataBase().observe(requireActivity()) {responseBase ->
            if (responseBase.status == Status.SUCCESS){
                getOrderListYear()
                var listMonthInput = requireActivity().resources.getStringArray(R.array.month)

                dialogMessageTitle(getStringRes(R.string.body_dialog_message_success))
            }else{
                dialogMessageDefault(getStringRes(R.string.error),
                    getStringRes(R.string.body_dialog_message_data),
                    1
                )
            }
        }
    }

    /**
     * Delete Year
     * */
    private fun setDeleteYear(yearItem:YearsEntity){
        viewModel.setDeleteYearDataBase(requireActivity(), yearItem)
        viewModel.getDeleteYearDataBase().observe(requireActivity()) {responseBase ->
            if (responseBase.status == Status.SUCCESS){
                dialogMessageTitle(getStringRes(R.string.success_delete))
                getOrderListYear()
            }else{
                dialogMessageDefault(getStringRes(R.string.error),
                    getStringRes(R.string.body_dialog_message_data),
                    1
                )
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        showOrHiddenMenuNavbar(true)
    }
}