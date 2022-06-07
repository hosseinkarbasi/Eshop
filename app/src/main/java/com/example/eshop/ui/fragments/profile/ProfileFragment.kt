package com.example.eshop.ui.fragments.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eshop.R
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.databinding.FragmentProfileBinding
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import com.example.eshop.utils.gone
import com.example.eshop.utils.visible
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
        logOut()

    }

    private fun logOut() {
        binding.logoutBtn.setOnClickListener {
            viewModel.insertUserEmail("", 0)
        }
    }

    private fun getUserInfo() {
        viewModel.getCustomer.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Success -> {
                    isSuccess()
                    binding.user = it.data[0]
                }
                is ResultWrapper.Loading -> {
                    isLoading()
                }
                is ResultWrapper.Error -> {
                    it.message?.let { it1 -> isError(it1) }
                }
            }
        }
    }

    private fun checkUser() {
        viewModel.pref.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            val userEmail = viewModel.pref.first()
            if (userEmail.email.isNotEmpty()) {
                viewModel.getCustomer(userEmail.email)
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

    private fun isLoading() = binding.apply {
        loading.visible()
        gpLayout.gone()
        loading.playAnimation()
    }

    private fun isError(errorMessage: String) = binding.apply {
        loading.gone()
        gpLayout.gone()
        loading.pauseAnimation()
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun isSuccess() = binding.apply {
        loading.gone()
        gpLayout.visible()
        loading.pauseAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}