package com.example.eshop.ui.fragments.profile

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eshop.R
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.databinding.FragmentProfileBinding
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DataBindingUtil.bind(view)

        settingsFragment()
        checkUser()

        binding.notification.setOnClickListener {
            viewModel.insertUserEmail("")
        }

    }

    private fun getUserInfo() {
        viewModel.getCustomer.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Success -> {
                    binding.user = it.data[0]
                }
                is ResultWrapper.Loading -> {}
                is ResultWrapper.Error -> {}
            }
        }
    }

    private fun checkUser() {
        viewModel.pref.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            val userEmail = viewModel.pref.first()
            if (userEmail.isNotEmpty()) {
                viewModel.getCustomer(userEmail)
                getUserInfo()
            } else {
                val action = ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun settingsFragment() {
        binding.settings.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToSettingsFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}