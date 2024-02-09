package com.control.paymentcontrol.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.control.paymentcontrol.R
import com.control.paymentcontrol.adapter.AdapterCarrousel
import com.control.paymentcontrol.adapter.AdapterCircle
import com.control.paymentcontrol.adapter.AdapterDataMonth
import com.control.paymentcontrol.database.entities.YearsEntity
import com.control.paymentcontrol.databinding.FragmentHomeBinding
import com.control.paymentcontrol.models.years.CreateNewYear
import com.control.paymentcontrol.repository.BaseRepository
import com.control.paymentcontrol.repository.response.YearItemRepository
import com.control.paymentcontrol.ui.base.BaseFragment
import com.control.paymentcontrol.ui.utils.OnActionButtonNavBar
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import java.util.Arrays

class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    private var selectPosition: Int = 0
    private lateinit var listYear: List<YearsEntity>
    private lateinit var yearRepository: YearItemRepository
    private lateinit var adapterCarrousel: AdapterCarrousel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        yearRepository = YearItemRepository(requireActivity())
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
            yearRepository.getAddYear(YearsEntity(0, "2024", "Enero - Febrero", "1500"))
            updateRecyclerViewCarousel()
        }



        return binding.root
    }

    fun recyclerViewCarousel(){
        adapterCarrousel = AdapterCarrousel(listYear)
        var adapterCircle = AdapterCircle(listYear.size,selectPosition,requireActivity())

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

    fun updateRecyclerViewCarousel(){
        listYear = yearRepository.getByAllYears()
        adapterCarrousel.updateSelect(listYear)
    }
}