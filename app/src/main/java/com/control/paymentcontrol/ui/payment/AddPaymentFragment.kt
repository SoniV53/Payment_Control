package com.control.paymentcontrol.ui.payment

import android.annotation.SuppressLint
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.control.paymentcontrol.R
import com.control.paymentcontrol.adapter.AdapterDataPayment
import com.control.paymentcontrol.databinding.FragmentAddPaymentBinding
import com.control.paymentcontrol.ui.base.BaseFragment
import com.control.paymentcontrol.ui.utils.OnActionButtonNavBarMenu
import com.control.paymentcontrol.ui.utils.PutArgumentsString.MONTH_SELECT
import com.control.paymentcontrol.ui.utils.PutArgumentsString.YEAR_SELECT
import com.control.paymentcontrol.viewmodels.ServicePaymentViewModel
import com.control.roomdatabase.entities.MonthEntity
import com.control.roomdatabase.entities.SpentEntity
import com.control.roomdatabase.entities.YearsEntity
import com.control.roomdatabase.utils.Status

class AddPaymentFragment : BaseFragment() {
    private lateinit var binding: FragmentAddPaymentBinding
    private lateinit var viewModel: ServicePaymentViewModel
    private lateinit var yearItem: YearsEntity
    private lateinit var monthItem: MonthEntity
    private lateinit var adapterPay: AdapterDataPayment
    private lateinit var spentList: List<SpentEntity>
    private var totalMonth = ""
    private var isDetails = false
    private var payMonthTotal = 0.00
    private var payMonth = 0.00
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ServicePaymentViewModel::class.java]
    }
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddPaymentBinding.inflate(inflater,container,false)

        yearItem = gson.fromJson(arguments?.getString(YEAR_SELECT),YearsEntity::class.java)
        monthItem = gson.fromJson(arguments?.getString(MONTH_SELECT),MonthEntity::class.java)

        binding.txtTitle.text = yearItem.name + " / " + monthItem.name
        binding.cpAmount.setText(monthItem.total)


        onClickMoreNavbar(object:OnActionButtonNavBarMenu{
            override fun onActionAddYear() {
                isDetails = false
                val bundle = Bundle()
                bundle.putString(MONTH_SELECT,gson.toJson(monthItem))
                findNavController().navigate(R.id.action_addPaymentFragment_to_formularyPaymentFragment,bundle)
            }

        })

        binding.consAction.setOnClickListener{
            moreDetails()
        }

        moreDetails()

        binding.cpAmount.setOnEditorActionListener { v, i, event ->
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {
                setUpdateMonth()

            }
            false
        }
        binding.cpAmount.setOnFocusChangeListener{ v, hasFocus ->
            if (!hasFocus) {
                setUpdateMonth()
                hideKeyboard(binding.root)
            }
        }

        binding.mainConstra.setOnClickListener {
            binding.cpAmount.clearFocus()
        }

        getOrderSpent()

        return binding.root
    }

    private fun recyclerViewData(){
        adapterPay = AdapterDataPayment(spentList,requireActivity(),object:AdapterDataPayment.OnClickButton{
            override fun onClickDelete(item: SpentEntity) {
                viewModel.deleteSpentStatus(requireActivity(),item)
                viewModel.getAddSpentDataBase().observe(requireActivity()) {responseBase ->
                    if (responseBase.status == Status.SUCCESS){
                        dialogMessageTitle(getStringRes(R.string.success_delete))
                        getOrderSpent()
                    }else{
                        dialogMessageDefault(getStringRes(R.string.error),
                            getStringRes(R.string.body_dialog_message_data),
                            1
                        )
                    }
                }
            }

            override fun onClickDetails(item: SpentEntity) {

            }

            override fun onEdit(item: SpentEntity, check: Boolean) {
                viewModel.updateAddSpentStatus(requireActivity(), item)
            }

        })

        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.adapter = adapterPay

    }

    /**
     * Update Month CONTROLLER
     */
    private fun setUpdateMonth(){
        if (binding.cpAmount.text.toString().isNotEmpty() && (!totalMonth.equals(binding.cpAmount.text.toString()) || totalMonth.isEmpty())){
            val updateMonth = monthItem
            updateMonth.total = binding.cpAmount.text.toString()
            viewModel.setUpdateMonthDataBase(requireActivity(), updateMonth)
            viewModel.getAddMonthDataBase().observe(requireActivity()) {responseBase ->
                if (responseBase.status == Status.SUCCESS){
                    dialogMessageTitle("Se Actualizo Monto")
                    totalMonth = binding.cpAmount.text.toString()
                }else{
                    dialogMessageDefault(getStringRes(R.string.error),
                        responseBase.message,
                        1
                    )
                }
            }
        }else {
            if (binding.cpAmount.text.toString().isEmpty()){
                binding.cpAmount.setText(totalMonth)
            }
        }

    }
    /**
     * Servicio que trae los gastos
     */
    private fun getOrderSpent(){
        viewModel.fullBySpent(requireActivity(),monthItem.id.toString()).observe(requireActivity()) {responseBase ->
            if (responseBase != null) {
                spentList = responseBase.spent
                recyclerViewData()
            }
        }
    }

    private fun calcDetails(){
        spentList.forEach{item ->

        }
    }

    private fun moreDetails() {
        binding.cpAmount.clearFocus()
        if (isDetails){
            binding.constMoreInfo.visibility = View.VISIBLE
            binding.imgViewDetails.setImageResource(R.drawable.chevron_up_solid)
            isDetails = false
        }else{
            binding.constMoreInfo.visibility = View.GONE
            binding.imgViewDetails.setImageResource(R.drawable.chevron_down_solid)
            isDetails = true
        }
    }


}