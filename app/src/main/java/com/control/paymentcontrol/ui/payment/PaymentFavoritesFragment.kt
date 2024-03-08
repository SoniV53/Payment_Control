package com.control.paymentcontrol.ui.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.control.paymentcontrol.R
import com.control.paymentcontrol.adapter.AdapterDataPayment
import com.control.paymentcontrol.databinding.FragmentHomeBinding
import com.control.paymentcontrol.databinding.FragmentPaymentFavoritesBinding
import com.control.paymentcontrol.models.AttributesDesign
import com.control.paymentcontrol.ui.base.BaseFragment
import com.control.paymentcontrol.ui.utils.OnActionButtonNavBarMenu
import com.control.paymentcontrol.ui.utils.OnClickInterface
import com.control.paymentcontrol.ui.utils.PutArgumentsString
import com.control.paymentcontrol.viewmodels.ServicePaymentViewModel
import com.control.roomdatabase.entities.MonthEntity
import com.control.roomdatabase.entities.SpentEntity
import com.control.roomdatabase.utils.Status
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Arrays

class PaymentFavoritesFragment : BaseFragment() {
    private lateinit var binding: FragmentPaymentFavoritesBinding
    private lateinit var viewModel: ServicePaymentViewModel
    private lateinit var adapterPay: AdapterDataPayment
    private lateinit var spentList: List<SpentEntity>
    private var type = 0
    private var idMonth = ""
    private var isSelectTodo = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ServicePaymentViewModel::class.java]

        type = arguments?.getInt(PutArgumentsString.TYPE_ENT)!!
        idMonth = arguments?.getString(PutArgumentsString.ID_MONTH)!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentFavoritesBinding.inflate(inflater,container,false)

        if (type == 1){
            showOrHiddenMenuNavbar(false)
        }
        binding.btnContinue.visibility = if (type == 1) View.VISIBLE else View.GONE
        binding.ckSelectTodo.visibility = if (type == 1) View.VISIBLE else View.GONE
        binding.txtSelect.visibility = if (type == 1) View.VISIBLE else View.GONE

        onClickMoreNavbar(object: OnActionButtonNavBarMenu {
            override fun onActionPositionOne() {
                val bundle = Bundle()
                bundle.putInt(PutArgumentsString.TYPE_ENT,0)
                bundle.putBoolean(PutArgumentsString.TYPE_EDIT,false)
                findNavController().navigate(R.id.action_paymentFavoritesFragment_to_formularyPaymentFragment,bundle)
            }
            override var attr: List<AttributesDesign>
                get() = Arrays.asList(
                    AttributesDesign(1,getStringRes(R.string.menu_add_spent),R.drawable.comment_dollar_solid),
                    AttributesDesign(2, visible = false)
                )
                set(value) {}
        })

        binding.ckSelectTodo.setOnCheckedChangeListener{bu,che ->
            val check = binding.ckSelectTodo.isChecked
            isSelectTodo = !check
            spentList.forEach{
                it.favorite = check
            }

            recyclerViewData()
            activateButton()
        }

        getOrderSpent()
        binding.tiSearch.addTextChangedListener {it->
            if (adapterPay != null){
                recyclerViewData()
                binding.empty.visibility = if (viewModel.filterSpentAllTitle(binding.tiSearch.text.toString(),spentList).size > 0) View.GONE else View.VISIBLE
            }
        }

        binding.btnContinue.setOnClickListener{
            spentList.forEach{
                if (it.favorite){
                    var newPay = it
                    newPay.idMonth = idMonth
                    newPay.id = 0

                    viewModel.setAddSpentDataBase(requireActivity(), newPay)
                    viewModel.getAddSpentDataBase().observe(requireActivity()) {responseBase ->
                        if (responseBase.status != Status.SUCCESS){
                            dialogMessageDefault(getStringRes(R.string.error),responseBase.message,1)
                        }
                    }
                }
            }
            dialogMessageTitle(getStringRes(R.string.body_dialog_message_success))
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    private fun recyclerViewData(){
        adapterPay = AdapterDataPayment(viewModel.filterSpentAllTitle(binding.tiSearch.text.toString(),spentList),requireActivity(),type,object: AdapterDataPayment.OnClickButton{
            override fun onClickDelete(item: SpentEntity) {
                dialogMessageOnAction(getStringRes(R.string.delete),getStringRes(R.string.delete_message),1,getStringRes(R.string.delete),object:
                    OnClickInterface {
                    override fun onClickAction() {
                        viewModel.deleteSpentStatus(requireActivity(),item).observe(requireActivity()) {responseBase ->
                            if (responseBase.status == Status.SUCCESS){
                                dialogMessageTitle(getStringRes(R.string.success_delete))
                                getOrderSpent()
                            }else{
                                dialogMessageDefault(getStringRes(R.string.error),responseBase.message,1)
                            }
                        }
                    }

                })

            }

            override fun onClickDetails(item: SpentEntity) {
                if (type == 0){
                    val bundle = Bundle()
                    bundle.putString(PutArgumentsString.PAYMENT_SELECT,gson.toJson(item))
                    bundle.putInt(PutArgumentsString.TYPE_ENT,0)
                    bundle.putBoolean(PutArgumentsString.TYPE_EDIT,true)
                    findNavController().navigate(R.id.action_paymentFavoritesFragment_to_formularyPaymentFragment,bundle)
                }else{
                    spentList.forEach{
                        if (item.title.equals(it.title))
                            it.favorite = item.favorite
                    }
                    activateButton()
                }
            }

        },false)

        binding.recyclerViewData.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerViewData.adapter = adapterPay

    }

    /**
     * Servicio que trae los gastos
     */
    private fun getOrderSpent(){
        viewModel.fullBySpentData(requireActivity()).observe(requireActivity()) {responseBase ->
            if (responseBase != null) {
                spentList = responseBase.toList()
                recyclerViewData()
            }
            binding.empty.visibility = if (spentList.size > 0) View.GONE else View.VISIBLE
        }
    }

    private fun activateButton(){
        var isActive = false;
        spentList.forEach{
            if (it.favorite){
                isActive = true
            }
        }
        binding.btnContinue.isEnabled = isActive
    }

    override fun onDestroy() {
        super.onDestroy()
        showOrHiddenMenuNavbar(true)
    }
}