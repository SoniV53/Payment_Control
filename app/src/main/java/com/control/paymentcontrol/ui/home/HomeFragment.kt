package com.control.paymentcontrol.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.control.paymentcontrol.R
import com.control.paymentcontrol.adapter.AdapterCarrousel
import com.control.paymentcontrol.adapter.AdapterCircle
import com.control.paymentcontrol.adapter.AdapterDataMonth
import com.control.paymentcontrol.databinding.FragmentHomeBinding
import com.control.paymentcontrol.ui.base.BaseFragment
import com.control.paymentcontrol.ui.utils.OnActionButtonNavBarMenu
import com.control.paymentcontrol.ui.utils.OnClickInterface
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
    private lateinit var yearItem:YearsEntity
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

        getOrderListYear()


        onClickMoreNavbar(object:OnActionButtonNavBarMenu{
            override fun onActionAddYear() {
                val items = viewModel.getYearList(requireActivity())

                MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(resources.getString(R.string.select_year))
                    .setItems(items) { _, which ->
                        setAddYear(items[which.inc() - 1])
                    }
                    .show()
            }

        })

        binding.addAction.setOnClickListener {
            getOrderNewListMonth()
        }

        binding.btnContinue.setOnClickListener {
            val items = viewModel.getYearList(requireActivity())

            MaterialAlertDialogBuilder(requireActivity())
                .setTitle(resources.getString(R.string.select_year))
                .setItems(items) { _, which ->
                    setAddYear(items[which.inc() - 1])
                }
                .show()
        }

        return binding.root
    }

    private fun recyclerViewCarousel(){
        adapterCarrousel = AdapterCarrousel(listYear,object: AdapterCarrousel.OnClickButton{
            override fun onClickDelete(item: YearsEntity,position: Int) {
                dialogMessageOnAction(getStringRes(R.string.delete),getStringRes(R.string.delete_message),1,getStringRes(R.string.delete),object:OnClickInterface{
                    override fun onClickAction() {
                        setDeleteYear(item)
                    }

                })
            }
        })

        adapterCircle = AdapterCircle(listYear.size,selectPosition,requireActivity())

        binding.carouselRecyclerview.adapter = adapterCarrousel
        binding.carouselRecyclerview.apply {
            set3DItem(false)
            setAlpha(true)
            setInfinite(false)
        }

        binding.carouselRecyclerview.scrollToPosition(positionCarrousel)
        binding.carouselRecyclerview.setItemSelectListener(object : CarouselLayoutManager.OnSelected {
            override fun onItemSelected(position: Int) {
                adapterCircle.updateSelect(position)
                positionCarrousel = position
                yearItem = gerYearItem()
                getOrderListMonth()
            }
        })


        binding.circlesRecyclerview.adapter = adapterCircle
        binding.circlesRecyclerview.apply {
            set3DItem(false)
            setAlpha(true)
            setInfinite(false)
            setFlat(false)
            setIsScrollingEnabled(true)
        }
    }

    private fun recyclerViewMonth(){
        adapterMonth = AdapterDataMonth(listMonth,requireActivity(),object: AdapterDataMonth.OnClickButton{
            override fun onClickDelete(item: MonthEntity) {
                viewModel.setDeleteMonthDataBase(requireActivity(), item)
                viewModel.getDeleteMonthDataBase().observe(requireActivity()) {responseBase ->
                    if (responseBase.status == SUCCESS){
                        getOrderListMonth()
                        dialogMessageTitle(getStringRes(R.string.body_dialog_delete_message_success))
                    }else{
                        dialogMessageDefault(getStringRes(R.string.error),
                            getStringRes(R.string.body_dialog_message_data),
                            1
                        )
                    }
                }
            }

            override fun onClickDetails(item: MonthEntity) {
                val bundle = Bundle()
                bundle.putString(YEAR_SELECT,gson.toJson( gerYearItem()))
                bundle.putString(MONTH_SELECT,gson.toJson(item))
                findNavController().navigate(R.id.action_homeFragment_to_addPaymentFragment,bundle)
            }

        })

        binding.dataRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.dataRecyclerView.adapter = adapterMonth

    }

    private fun getOrderListYear(){
        viewModel.fullByYears(requireActivity()).observe(requireActivity()) {responseBase ->
            if (responseBase != null) {
                listYear = responseBase
                binding.constrainEmpty.visibility = if (listYear.isNotEmpty()) View.GONE else View.VISIBLE
                binding.mainConstra.visibility = if (listYear.isEmpty()) View.GONE else View.VISIBLE
                recyclerViewCarousel()
                getOrderListMonth()
            }
        }
    }

    /**
     * ADD NEW YEAR CONTROLLER
     */
    private fun setAddYear(yearItem:String){
        viewModel.setAddYearDataBase(requireActivity(), YearsEntity(yearItem))
        viewModel.getAddYearDataBase().observe(requireActivity()) {responseBase ->
            if (responseBase.status == SUCCESS){
                getOrderListYear()
                var listMonthInput = requireActivity().resources.getStringArray(R.array.month)
                /*listMonthInput.forEach { name ->
                    setAddMonth(name,responseBase.)
                }*/
                println("RESPONSE DATA: "+responseBase.message)

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
            if (responseBase.status == SUCCESS){
                dialogMessageTitle(getStringRes(R.string.success_delete))
                getOrderListYear()
                positionCarrousel = if (listYear.isNotEmpty()) 0 else -1
            }else{
                dialogMessageDefault(getStringRes(R.string.error),
                    getStringRes(R.string.body_dialog_message_data),
                    1
                )
            }
        }
    }


    private fun getOrderListMonth(){
        viewModel.fullByMonths(requireActivity(),gerYearItem().id.toString()).observe(requireActivity()) {responseBase ->
            if (responseBase != null) {
                val result = mutableListOf<MonthEntity>()
                result.addAll(responseBase)
                result.add(MonthEntity("",""))
                listMonth = result
                listMonthOriginal = responseBase
                recyclerViewMonth()
            }
            binding.emptyMonth.visibility = if (listMonthOriginal.isEmpty()) View.VISIBLE else View.GONE
            binding.dataRecyclerView.visibility = if (listMonthOriginal.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun getOrderNewListMonth(){
        var items = requireActivity().resources.getStringArray(R.array.month)
        var list = viewModel.getValidExistMonth(items,listMonth)

        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(resources.getString(R.string.select_month))
            .setItems(list) { _, which ->
                setAddMonth(list[which.inc() - 1],gerYearItem().id.toString())
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


    private fun gerYearItem():YearsEntity{
        if ( listYear != null && listYear.isNotEmpty() && positionCarrousel <= listYear.size){
            return listYear[positionCarrousel]
        }
        return YearsEntity("")
    }

}