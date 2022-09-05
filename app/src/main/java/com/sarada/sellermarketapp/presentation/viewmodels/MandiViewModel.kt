package com.sarada.sellermarketapp.presentation.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import com.sarada.sellerapp.MainData

class MandiViewModel: ViewModel(){

    var sellerName: String = ""
    var loyaltyId: String = ""
    var villageName: String = ""
    var weight: Double = 0.00
    var grossPrice = 0.00

    var dataModel: MainData? = null
    var isRegisteredUser = false
    var isAutoFillData = false

    init{}

    fun calculateLoyaltyIndex(): Double{
        var loyaltyIndex = 0.98
        dataModel?.sellers?.forEach { seller ->
            if (sellerName == seller.sellerName && seller.isRegistered == true) {
                loyaltyIndex = 1.12
                isRegisteredUser = true
                return@forEach
            }
        }
        return loyaltyIndex
    }

    fun calculateVillagePrice(): Double {
        dataModel?.villageData?.forEach { village ->
            if (village.villageName == villageName)
                return village.producePrice ?: 1.00
        }
        return 1.00
    }

    fun isDataValid() =
        if (isRegisteredUser)
            sellerName.isNotBlank() && loyaltyId.isNotBlank()
        else sellerName.isNotBlank()

    fun calculateGrossPrice(weight: String){
        if (isDataValid()) {

            val loyaltyIndex = calculateLoyaltyIndex()
            this.weight = weight.toDouble()
            val villagePrice = calculateVillagePrice()

            val grossPrice = loyaltyIndex * this.weight * villagePrice
            this.grossPrice = String.format("%.2f", grossPrice).toDouble()
        }
    }

    var updateSellerDetails = { sellerName: String, loyaltyCardId: String ->
        this.sellerName = sellerName
        this.loyaltyId = loyaltyCardId
    }

    var onVillageSelected = { villageName: String ->
        this.villageName = villageName
    }
}