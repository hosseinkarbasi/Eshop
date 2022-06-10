package com.example.eshop.ui.fragments.login

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eshop.R
import com.example.eshop.data.local.model.User
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.databinding.FragmentLoginBinding
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        login()
        getUserInfo()
        goToSignUp()
        goToProfile()
    }

    private fun goToProfile() {
        viewModel.pref.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            val userEmail = viewModel.pref.first()
            if (userEmail.email.isNotEmpty()) {
                val action = LoginFragmentDirections.actionLoginFragmentToProfileFragment()
                findNavController().navigate(action)
            }
        }
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

    private suspend fun isSuccess(data: List<User>) {
        if (data.isNotEmpty()) {
            viewModel.saveUserInfo(data[0].email, data[0].id)
            delay(100)
            val action = LoginFragmentDirections.actionLoginFragmentToProfileFragment()
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(), "ایمیل وارد شده صحیح نیست", Toast.LENGTH_LONG).show()
        }
    }

    private fun goToSignUp() {
        binding.createAccount.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            findNavController().navigate(action)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}