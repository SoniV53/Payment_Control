package com.control.paymentcontrol.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.control.paymentcontrol.R
import com.control.paymentcontrol.adapter.AdapterCarrousel
import com.control.paymentcontrol.adapter.AdapterCircle
import com.control.paymentcontrol.databinding.FragmentHomeBinding
import com.control.paymentcontrol.ui.base.BaseFragment
import com.control.paymentcontrol.ui.utils.OnActionButtonNavBar
import com.control.paymentcontrol.viewmodels.ServicePaymentViewModel
import com.control.roomdatabase.entities.YearsEntity
import com.control.roomdatabase.repository.ui.YearItemRepository
import com.control.roomdatabase.utils.Status.SUCCESS
import com.example.awesomedialog.AwesomeDialog
import com.example.awesomedialog.background
import com.example.awesomedialog.body
import com.example.awesomedialog.icon
import com.example.awesomedialog.onNegative
import com.example.awesomedialog.onPositive
import com.example.awesomedialog.title
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager

class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    private var selectPosition: Int = 0
    private lateinit var listYear: List<YearsEntity>
    private lateinit var yearRepository: YearItemRepository
    private lateinit var adapterCarrousel: AdapterCarrousel
    private lateinit var adapterCircle: AdapterCircle
    private lateinit var viewModel: ServicePaymentViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        yearRepository = YearItemRepository(requireActivity())
        viewModel = ViewModelProvider(requireActivity())[ServicePaymentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        listYear = yearRepository.getByAllYears()
        recyclerViewCarousel()

        onClickMoreNavbar(object:OnActionButtonNavBar{
            override fun onActionMore() {
                Toast.makeText(requireContext(), "Prueba", Toast.LENGTH_SHORT).show()
            }
        })

        binding.addAction.setOnClickListener {
            setAddYear()
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
    private fun setAddYear(){
        viewModel.setAddYearDataBase(requireActivity(), YearsEntity(0,"","",""))
        viewModel.getAddYearDataBase().observe(requireActivity()) {responseBase ->
            if (responseBase.status == SUCCESS){
                updateRecyclerViewCarousel()
            }else{
                AwesomeDialog.build(requireActivity())
                    .title("Error")
                    .body("Faltan Datos que completar")
                    .icon(R.drawable.exclamation_triangle_solid)
                    .onPositive("OK",R.color.accent) {
                        Log.d("TAG", "positive ")
                    }
            }
        }
    }
    fun updateRecyclerViewCarousel(){
        listYear = yearRepository.getByAllYears()
        adapterCarrousel.updateSelect(listYear)
        adapterCircle.updateSize(listYear.size)
    }
}