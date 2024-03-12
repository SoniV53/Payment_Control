package com.control.paymentcontrol.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.control.paymentcontrol.component.adapter.AdapterTableData
import com.control.paymentcontrol.component.models.ColumnsRowModel
import com.control.paymentcontrol.component.models.RowTableModel
import com.control.paymentcontrol.databinding.FragmentGenerateTableBinding
import com.control.paymentcontrol.ui.base.BaseFragment


class GenerateTableFragment : BaseFragment() {

    private lateinit var binding:FragmentGenerateTableBinding
    private lateinit var adapter: AdapterTableData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGenerateTableBinding.inflate(inflater,container,false)
        recyclerViewMonth()

        return binding.root
    }

    private fun recyclerViewMonth(){
        var list = createColumns()
        adapter = AdapterTableData(list.toList(),requireActivity())

        val layoutManager = GridLayoutManager(requireActivity(),7)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                when (position) {
                    0,2 -> return 1
                    3 -> return 3
                    else -> return 2
                }
            }
        }

        binding.recyclerTableData.layoutManager = layoutManager
        binding.recyclerTableData.adapter = adapter

    }

    private fun createColumns():List<ColumnsRowModel>{
        val listColumns = mutableListOf<ColumnsRowModel>()

        listColumns.add(ColumnsRowModel(0,"ID", row = createRow()))
        listColumns.add(ColumnsRowModel(1,"TITULO",row = createRow()))
        listColumns.add(ColumnsRowModel(2,"ID",row = createRow()))
        listColumns.add(ColumnsRowModel(3,"DESCRIPCION",row = createRow()))


        return listColumns
    }

    private fun createRow():List<RowTableModel>{
        val listColumns = mutableListOf<RowTableModel>()
        listColumns.add(RowTableModel("1"))
        listColumns.add(RowTableModel("2"))
        listColumns.add(RowTableModel("3"))

        return listColumns
    }

}