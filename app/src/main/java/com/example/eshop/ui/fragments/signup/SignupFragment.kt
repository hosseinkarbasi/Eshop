package com.example.eshop.ui.fragments.signup

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
import com.example.eshop.databinding.FragmentSignupBinding
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SignupFragment : Fragment(R.layout.fragment_signup) {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SignupViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignupBinding.bind(view)

        createCustomer()
    }

    private fun createCustomer() {
        binding.signUpBtn.setOnClickListener {

            if (!validateFirstName() || !validateLastName() || !validateEmail()) {
                return@setOnClickListener
            } else {

                val customer = User(
                    0,
                    binding.firstNameEt.text.toString(),
                    binding.lastNameEt.text.toString(),
                    binding.emailEt.text.toString()
                )
                viewModel.createCustomer(customer)
                getCustomerInfo()
            }
        }
    }

    private fun getCustomerInfo() {
        viewModel.getCustomerInfo.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Error -> {
                    Toast.makeText(requireContext(), "شکست خورد", Toast.LENGTH_SHORT).show()
                }
                is ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
                    viewModel.insertUserEmail(it.data.email, it.data.id)
                    Toast.makeText(
                        requireContext(),
                        "حساب شما با موفقیت ایجاد شد",
                        Toast.LENGTH_SHORT
                    ).show()
                    delay(1000)
                    val action = SignupFragmentDirections.actionSignupFragmentToProfileFragment()
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun validateEmail(): Boolean {
        return if (binding.emailEt.text?.isEmpty() == true) {
            binding.emailLayout.error = "ایمیل را وارد کنید"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEt.text.toString())
                .matches()
        ) {
            binding.emailLayout.error = "ایمیل نادرست است"
            false
        } else {
            binding.emailLayout.error = null
            true
        }
    }

    private fun validateFirstName(): Boolean {
        return if (binding.firstNameEt.text?.isEmpty() == true) {
            binding.firsNameLayout.error = "لطفا نام خود را وارد کنید"
            false
        } else {
            binding.firsNameLayout.error = null
            true
        }
    }

    private fun validateLastName(): Boolean {
        return if (binding.lastNameEt.text?.isEmpty() == true) {
            binding.lastNameLayout.error = "لطفا نام خانوادکی خود را وارد کنید"
            false
        } else {
            binding.lastNameLayout.error = null
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}