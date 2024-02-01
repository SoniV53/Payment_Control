package com.control.paymentcontrol.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.control.paymentcontrol.R
import com.control.paymentcontrol.adapter.AdapterCarrousel
import com.control.paymentcontrol.adapter.AdapterCircle
import com.control.paymentcontrol.adapter.AdapterDataMonth
import com.control.paymentcontrol.databinding.FragmentHomeBinding
import com.control.paymentcontrol.models.years.CreateNewYear
import com.control.paymentcontrol.ui.base.BaseFragment
import com.control.paymentcontrol.ui.utils.OnActionButtonNavBar
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import java.util.Arrays

class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    private var selectPosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        onClickMoreNavbar(object:OnActionButtonNavBar{
            override fun onActionMore() {
                Toast.makeText(requireContext(), "Prueba", Toast.LENGTH_SHORT).show()
            }
        })
        val listYear = mutableListOf<CreateNewYear>()
        listYear.add(CreateNewYear("2024","Enero - Febrero","Q123"))
        listYear.add(CreateNewYear("2025","Enero - Febrero","Q123"))
        listYear.add(CreateNewYear("2026","Enero - Febrero","Q123"))

        val listMonth = mutableListOf<CreateNewYear>()
        listMonth.add(CreateNewYear("Enero","Q123","Q123"))
        listMonth.add(CreateNewYear("Febrero","Q123","Q123"))

        val listMonth2 = mutableListOf<CreateNewYear>()

        listMonth2.add(CreateNewYear("Enero","Q123","Q123"))
        listMonth2.add(CreateNewYear("Febrero","Q123","Q123"))
        listMonth2.add(CreateNewYear("Marzo","Q123","Q123"))
        listMonth2.add(CreateNewYear("Marzo","Q123","Q123"))
        listMonth2.add(CreateNewYear("Marzo","Q123","Q123"))
        listMonth2.add(CreateNewYear("Marzo","Q123","Q123"))
        listMonth2.add(CreateNewYear("Marzo","Q123","Q123"))
        listMonth2.add(CreateNewYear("Marzo","Q123","Q123"))
        listMonth2.add(CreateNewYear("Marzo","Q123","Q123"))
        listMonth2.add(CreateNewYear("Marzo","Q123","Q123"))
        listMonth2.add(CreateNewYear("Marzo","Q123","Q123"))
        listMonth2.add(CreateNewYear("Marzo","Q123","Q123"))
        listMonth2.add(CreateNewYear(" ","Q123","Q123"))

        val listMonth3 = Arrays.asList(resources.getStringArray(R.array.month))

        var adapter = AdapterCarrousel(listYear)
        var adapterMonth = AdapterDataMonth(listMonth)
        var adapterCircle = AdapterCircle(listYear.size,selectPosition,requireActivity())

        binding.carouselRecyclerview.adapter = adapter
        binding.carouselRecyclerview.apply {
            set3DItem(false)
            setAlpha(true)
            setInfinite(false)
        }

        binding.carouselRecyclerview.setItemSelectListener(object : CarouselLayoutManager.OnSelected {
            override fun onItemSelected(position: Int) {
                adapterCircle.updateSelect(position)
                adapterMonth.updateData(listMonth2)
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

        binding.dataRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.dataRecyclerView.setHasFixedSize(true)
        binding.dataRecyclerView.adapter = adapterMonth

        return binding.root
    }

}