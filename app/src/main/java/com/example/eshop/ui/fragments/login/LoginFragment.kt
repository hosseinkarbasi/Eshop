package com.example.eshop.ui.fragments.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eshop.R
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.databinding.FragmentLoginBinding
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        binding.loginBtn.setOnClickListener {
            val email = binding.emailEd.text.toString()
            viewModel.getCustomer(email)
        }
        getUserInfo()
        goToSignUp()

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
                    viewModel.saveUserInfo(it.data[0].email, it.data[0].id)
                    delay(100)
                    val action = LoginFragmentDirections.actionLoginFragmentToProfileFragment()
                    findNavController().navigate(action)
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