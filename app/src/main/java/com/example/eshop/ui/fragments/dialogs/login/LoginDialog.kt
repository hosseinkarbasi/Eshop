package com.example.eshop.ui.fragments.dialogs.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eshop.R
import com.example.eshop.data.local.model.User
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.databinding.FragmentLoginDialogBinding
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class LoginDialog : BottomSheetDialogFragment() {

    private var _binding: FragmentLoginDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LoginDialogViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginDialogBinding.inflate(inflater, container, false)

        login()
        getUserInfo()

        return binding.root
    }

    private fun login() {
        binding.loginBtn.setOnClickListener {
            if (!validateEmail()) {
                return@setOnClickListener
            } else {
                val email = binding.emailEd.text.toString()
                viewModel.getCustomer(email)

            }
        }
    }

    private fun validateEmail(): Boolean {
        return if (binding.emailEd.text?.isEmpty() == true) {
            binding.emailLoginLayout.error = "ایمیل را وارد کنید"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEd.text.toString())
                .matches()
        ) {
            binding.emailLoginLayout.error = "ایمیل نادرست است"
            false
        } else {
            binding.emailLoginLayout.error = null
            true
        }
    }

    private fun getUserInfo() {
        viewModel.getCustomer.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Success -> {
                    isSuccess(it.data)
                }
                is ResultWrapper.Loading -> {}
                is ResultWrapper.Error -> {
                    binding.emailEd.error = "ایمیل وارد شده صحیح نیست"
                }
            }
        }
    }

    private suspend fun isSuccess(data: List<User>) {
        if (data.isNotEmpty()) {
            viewModel.saveUserInfo(data[0].email, data[0].id)
            delay(100)
            val action = LoginDialogDirections.actionLoginDialogToPaymentFragment2()
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(), "ایمیل وارد شده صحیح نیست", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getTheme(): Int {
        return  R.style.BottomSheetDialogTheme
    }
}