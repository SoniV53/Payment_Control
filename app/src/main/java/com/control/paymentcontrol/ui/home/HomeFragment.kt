package com.control.paymentcontrol.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.control.paymentcontrol.R
import com.control.paymentcontrol.adapter.AdapterCarrousel
import com.control.paymentcontrol.adapter.AdapterCircle
import com.control.paymentcontrol.databinding.FragmentHomeBinding
import com.control.paymentcontrol.ui.base.BaseFragment
import com.control.paymentcontrol.ui.utils.OnActionButtonNavBarMenu
import com.control.paymentcontrol.viewmodels.ServicePaymentViewModel
import com.control.roomdatabase.entities.YearsEntity
import com.control.roomdatabase.repository.ui.YearItemRepository
import com.control.roomdatabase.utils.Status.SUCCESS
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import java.util.Calendar

class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    private var selectPosition: Int = 0
    private lateinit var listYear: List<YearsEntity>
    private lateinit var adapterCarrousel: AdapterCarrousel
    private lateinit var adapterCircle: AdapterCircle
    private lateinit var viewModel: ServicePaymentViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ServicePaymentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        listYear = viewModel.fullByYears(requireActivity())
        recyclerViewCarousel()

        onClickMoreNavbar(object:OnActionButtonNavBarMenu{
            override fun onActionAddYear() {
                //setAddYear()
                //val items = arrayOf("Item 1", "Item 2", "Item 3")
                val items = viewModel.getYearList(requireActivity())

                MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(resources.getString(R.string.select_year))
                    .setItems(items) { dialog, which ->
                        setAddYear(items[which.inc() - 1])
                    }
                    .show()
            }

        })

        binding.addAction.setOnClickListener {
            viewModel.getYearList(requireActivity())
        }



        return binding.root
    }

    fun recyclerViewCarousel(){
        adapterCarrousel = AdapterCarrousel(listYear)
        adapterCircle = AdapterCircle(listYear.size,selectPosition,requireActivity())

        binding.carouselRecyclerview.adapter = adapterCarrousel
        binding.carouselRecyclerview.apply {
            set3DItem(false)
            setAlpha(true)
            setInfinite(false)
        }

        binding.carouselRecyclerview.setItemSelectListener(object : CarouselLayoutManager.OnSelected {
            override fun onItemSelected(position: Int) {
                adapterCircle.updateSelect(position)
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

    /**
     * ADD NEW YEAR CONTROLLER
     */
    private fun setAddYear(yearItem:String){
        viewModel.setAddYearDataBase(requireActivity(), YearsEntity(yearItem))
        viewModel.getAddYearDataBase().observe(requireActivity()) {responseBase ->
            if (responseBase.status == SUCCESS){
                updateRecyclerViewCarousel()
                dialogMessageDefault()
            }else{
                dialogMessageDefault(
                    getStringRes(R.string.body_dialog_message_data),
                    1
                )
            }
        }
    }
    fun updateRecyclerViewCarousel(){
        listYear = viewModel.fullByYears(requireActivity())
        adapterCarrousel.updateSelect(listYear)
        adapterCircle.updateSize(listYear.size)
    }
}