package com.sarada.sellerapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.sarada.sellermarketapp.databinding.EdittextItemLayoutBinding
import com.sarada.sellermarketapp.databinding.EdittextWithTextItemLayoutBinding
import com.sarada.sellermarketapp.databinding.SpinnerItemLayoutBinding


class ViewItemsAdapter(
    private val mContext: Context?,
    private val onSellerNameUpdated: (value: String) -> Unit,
    private val onLoyaltyTextUpdated: (value: String) -> Unit,
    private val onWeightUpdated: (value: String) -> Unit,
    private val onVillageSelected: (value: String) -> Unit,
    private val viewItems: ArrayList<ViewItem>?
) :
    ListAdapter<ViewItem, ViewItemsAdapter.ItemsViewHolder>(ViewItemsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        return ItemsViewHolder.from(
            mContext!!,
            parent,
            viewType,
            onSellerNameUpdated,
            onLoyaltyTextUpdated,
            onWeightUpdated,
            onVillageSelected
        )
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        return when(viewItems?.get(position)?.type) {
            "editText" -> 0
            "spinner" -> 1
            "editTextWithText" -> 2
            else -> -1
        }
    }

    class ItemsViewHolder private constructor(
        private val mContext: Context,
        private val binding: ViewBinding,
        private val onSellerNameUpdated: (value: String) -> Unit,
        private val onLoyaltyTextUpdated: (value: String) -> Unit,
        private val onWeightUpdated: (value: String) -> Unit,
        private val onVillageSelected: (value: String) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {}

        fun bind(
            item: ViewItem
        ) {
            when(itemViewType){
                0 -> {
                    val vBinding = binding as EdittextItemLayoutBinding
                    vBinding.apply {
                        tvTitle.text = item.title
                        etValue.hint = item.placeHolder
                        if(item.title == "Seller name")
                            etValue.doAfterTextChanged { onSellerNameUpdated(etValue.text.toString()) }
                        else
                            etValue.doAfterTextChanged { onLoyaltyTextUpdated(etValue.text.toString()) }
                        etValue.setText(item.value)
                        etValue.visibility = View.VISIBLE
                    }
                }
                1 -> {
                    val vBinding = binding as SpinnerItemLayoutBinding
                    vBinding.apply {
                        tvTitle.text = item.title
                        spValue.adapter = ArrayAdapter<Any?>(
                            mContext,
                            android.R.layout.simple_spinner_dropdown_item,
                            item.villages as List<String>
                        )
                        spValue.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                            override fun onNothingSelected(parent: AdapterView<*>?) {}

                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                onVillageSelected(item.villages?.get(position) ?: "")
                            }

                        }
                        spValue.visibility = View.VISIBLE

                    }
                }
                2 -> {
                    val vBinding = binding as EdittextWithTextItemLayoutBinding
                    vBinding.apply {
                        tvTitle.text = item.title
                        etWeight.doAfterTextChanged { onWeightUpdated(etWeight.text.toString()) }
                    }
                }
            }
        }

        companion object {
            fun from(
                mContext: Context,
                parent: ViewGroup,
                viewType: Int,
                onSellerNameUpdated: (value: String) -> Unit,
                onLoyaltyTextUpdated: (value: String) -> Unit,
                onWeightUpdated: (value: String) -> Unit,
                onVillageSelected: (value: String) -> Unit
            ): ItemsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = when(viewType){
                    0 -> EdittextItemLayoutBinding.inflate(layoutInflater, parent, false)
                    1 -> SpinnerItemLayoutBinding.inflate(layoutInflater, parent, false)
                    2 -> EdittextWithTextItemLayoutBinding.inflate(layoutInflater, parent, false)
                    else -> EdittextItemLayoutBinding.inflate(layoutInflater, parent, false)
                }
                return ItemsViewHolder(
                    mContext,
                    binding,
                    onSellerNameUpdated,
                    onLoyaltyTextUpdated,
                    onWeightUpdated,
                    onVillageSelected
                )
            }
        }
    }
}

class ViewItemsDiffCallback: DiffUtil.ItemCallback<ViewItem>() {
    override fun areItemsTheSame(oldItem: ViewItem, newItem: ViewItem): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: ViewItem, newItem: ViewItem): Boolean {
        return oldItem == newItem
    }
}