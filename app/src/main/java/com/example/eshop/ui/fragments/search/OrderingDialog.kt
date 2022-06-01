package com.example.eshop.ui.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.eshop.R
import com.example.eshop.data.remote.Constants.DATE
import com.example.eshop.data.remote.Constants.PRICE
import com.example.eshop.data.remote.Constants.PRICE_ASC
import com.example.eshop.data.remote.Constants.PRICE_DESC
import com.example.eshop.data.remote.Constants.RATING
import com.example.eshop.databinding.DialogOrderingBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OrderingDialog(private val ordering: (SortingText) -> Unit) :
    BottomSheetDialogFragment() {

    private var _binding: DialogOrderingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogOrderingBinding.inflate(inflater, container, false)

        binding.orderingRadio.setOnCheckedChangeListener { _, checkedId ->
            val order = when (checkedId) {
                binding.newestRadio.id -> SortingText.DATE
                binding.bestsellingRadio.id -> SortingText.RATING
                binding.pricesHighToLowRadio.id -> SortingText.PRICE_DESC
                binding.pricesLowToHighRadio.id -> SortingText.PRICE_ASC
                else -> {
                    SortingText.DATE
                }
            }
            ordering(order)
            dismiss()
        }

        binding.btnNegative.setOnClickListener { dismiss() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }
}