package com.sarada.sellermarketapp.presentation.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sarada.sellermarketapp.R
import com.sarada.sellermarketapp.databinding.SellerSuccessFragmentBinding

class SellerSuccessFragment : Fragment() {
    lateinit var binding: SellerSuccessFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SellerSuccessFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    private fun setUp() {
        binding.run {
            arguments?.let {
                val name = it.getString("name") ?: ""
                val weight = it.getDouble("weight").toInt()
                val grossPrice = it.getDouble("gross_price")

                tvSellerName.text = getString(R.string.thank_you_for_selling, name)
                tvWeightPrice.text = getString(R.string.please_ensure_you, grossPrice, weight)

                btnSell.setOnClickListener {
                    activity?.onBackPressed()
                }
            }
        }
    }
}