package com.example.eshop.ui.fragments.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.eshop.R
import com.example.eshop.data.local.data_store.Theme
import com.example.eshop.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SettingsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        updateTheme()
    }

    private fun updateTheme()=binding.apply {
        radioTheme.setOnCheckedChangeListener { _, checkedId ->
            val theme = when (checkedId) {
                light.id -> Theme.LIGHT
                night.id -> Theme.NIGHT
                else -> Theme.AUTO
            }
            viewModel.updateTheme(theme)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}