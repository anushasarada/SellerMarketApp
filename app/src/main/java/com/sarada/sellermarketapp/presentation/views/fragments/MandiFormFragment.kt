package com.sarada.sellermarketapp.presentation.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sarada.sellerapp.MainData
import com.sarada.sellerapp.Seller
import com.sarada.sellerapp.ViewItemsAdapter
import com.sarada.sellermarketapp.databinding.MandiFormFragmentBinding
import com.sarada.sellermarketapp.presentation.getJsonDataFromAsset
import com.sarada.sellermarketapp.presentation.viewmodels.MandiViewModel
import com.sarada.sellermarketapp.presentation.views.activities.SellerMarketActivity

class MandiFormFragment : Fragment() {

    lateinit var binding: MandiFormFragmentBinding
    private var viewItemsAdapter: ViewItemsAdapter? = null

    private var sellerNameIndex: Int = 0
    private var loyaltyNumberIndex: Int = 1

    private val mandiViewModel: MandiViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MandiFormFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
        initViews()
    }

    private fun loadData() {
        val jsonFileString =
            activity?.applicationContext?.let { getJsonDataFromAsset(it, "jsonFile.json") }
        val gson = Gson()
        val jsonData = object : TypeToken<MainData>() {}.type
        mandiViewModel.dataModel = gson.fromJson(jsonFileString, jsonData)

        sellerNameIndex = mandiViewModel.dataModel?.sellerNameIndex!!
        loyaltyNumberIndex = mandiViewModel.dataModel?.loyaltyNumberIndex!!
    }

    private fun initViews() {
        viewItemsAdapter = ViewItemsAdapter(
            activity?.applicationContext,
            onSellerNameUpdated,
            onLoyaltyTextUpdated,
            onWeightUpdated,
            mandiViewModel.onVillageSelected,
            mandiViewModel.dataModel?.viewItems
        )

        binding.rvViewItems.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = viewItemsAdapter
            smoothScrollToPosition(0)
        }
        viewItemsAdapter?.submitList(mandiViewModel.dataModel?.viewItems)

        binding.btnSellProduce.setOnClickListener {
            if (mandiViewModel.isDataValid()) {
                (activity as? SellerMarketActivity)?.sellApples(
                    bundleOf(
                        Pair("name", mandiViewModel.sellerName),
                        Pair("weight", mandiViewModel.weight),
                        Pair("gross_price", mandiViewModel.grossPrice)
                    )
                )
            }
        }
    }

    private var onSellerNameUpdated = { value: String ->
        if (!binding.rvViewItems.isComputingLayout) {

            if (!mandiViewModel.isAutoFillData) {
                mandiViewModel.dataModel?.sellers?.forEach { seller ->
                    if (value == seller.sellerName) {

                        updateDataAndUi(loyaltyNumberIndex, seller.loyaltyCardId, seller)
                        return@forEach
                    }
                }
            } else {
                /*dataModel?.viewItems?.get(loyaltyNumberIndex)?.value = ""
                viewItemsAdapter?.notifyItemChanged(loyaltyNumberIndex)*/
                mandiViewModel.isAutoFillData = false
            }
        }
        Unit
    }

    private var onLoyaltyTextUpdated = { value: String ->
        if (!binding.rvViewItems.isComputingLayout) {

            if (!mandiViewModel.isAutoFillData) {
                mandiViewModel.dataModel?.sellers?.forEach { seller ->
                    if (value == seller.loyaltyCardId) {

                        updateDataAndUi(sellerNameIndex, seller.sellerName, seller)
                        return@forEach
                    }
                }
            } else {
                /*dataModel?.viewItems?.get(sellerNameIndex)?.value = ""
                viewItemsAdapter?.notifyItemChanged(sellerNameIndex)*/
                mandiViewModel.isAutoFillData = false
            }
        }
        Unit
    }

    private fun updateDataAndUi(index: Int, value: String?, seller: Seller) {

        mandiViewModel.dataModel?.viewItems?.get(index)?.value = value
        viewItemsAdapter?.notifyItemChanged(index)

        mandiViewModel.updateSellerDetails(seller.sellerName ?: "", seller.loyaltyCardId ?: "")

        calculateLoyaltyIndex()

        mandiViewModel.isAutoFillData = true
    }

    private fun calculateLoyaltyIndex(): Double {

        binding.tvLoyaltyIndex.text = "Applied loyalty index : ${mandiViewModel.calculateLoyaltyIndex()}"
        return mandiViewModel.calculateLoyaltyIndex()
    }

    private var onWeightUpdated = { weight: String ->

        mandiViewModel.calculateGrossPrice(weight)
        binding.tvGrossPriceValue.text = mandiViewModel.grossPrice.toString()
    }
}