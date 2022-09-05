package com.sarada.sellerapp

import androidx.constraintlayout.widget.Placeholder
import com.google.gson.annotations.SerializedName

data class MainData (

        @SerializedName("viewItems"      ) var viewItems      : ArrayList<ViewItem> = arrayListOf(),
        @SerializedName("villageData"    ) var villageData    : ArrayList<VillageData> = arrayListOf(),
        @SerializedName("sellers"        ) var sellers        : ArrayList<Seller>     = arrayListOf(),
        @SerializedName("sellerNameIndex"        ) var sellerNameIndex        : Int,
        @SerializedName("loyaltyNumberIndex"        ) var loyaltyNumberIndex        : Int

)

data class ViewItem (

        @SerializedName("type"        ) var type        : String,
        @SerializedName("title"       ) var title       : String,
        @SerializedName("placeHolder" ) var placeHolder : String? = null,
        @SerializedName("bgColor"     ) var bgColor     : String? = null,
        @SerializedName("villages"     ) var villages     : List<String>? = null,
        @SerializedName("quantifier"     ) var quantifier     : String? = null,
        @SerializedName("value") var value: String? = null

)

data class VillageData (

        @SerializedName("villageName"  ) var villageName  : String? = null,
        @SerializedName("producePrice" ) var producePrice : Double? = null

)

data class Seller (

        @SerializedName("sellerName"    ) var sellerName    : String?  = null,
        @SerializedName("isRegistered"  ) var isRegistered  : Boolean? = null,
        @SerializedName("loyaltyCardId" ) var loyaltyCardId : String?  = null

)