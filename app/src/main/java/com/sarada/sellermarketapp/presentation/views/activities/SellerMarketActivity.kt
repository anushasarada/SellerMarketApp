package com.sarada.sellermarketapp.presentation.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.sarada.sellermarketapp.R
import com.sarada.sellermarketapp.databinding.SellerMarketActivityBinding

class SellerMarketActivity : AppCompatActivity() {

    lateinit var binding: SellerMarketActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SellerMarketActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun sellApples(bundle: Bundle) {
        val navController = binding.navHostFragment.findNavController()
        navController.navigate(R.id.action_mandiFormFragment_to_sellerSuccessFragment, bundle)
    }
}