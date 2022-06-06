package com.example.eshop.ui.fragments.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.eshop.R
import com.example.eshop.databinding.FragmentHomecartBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeCartFragment : Fragment(R.layout.fragment_homecart) {

    private var _binding: FragmentHomecartBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomecartBinding.bind(view)

        viewPager()

    }

    private fun viewPager() {
        val tabTitle = arrayOf("سبد خرید", "سفارشات")
        binding.viewPager.adapter = CartViewPagerAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(binding.Tabs, binding.viewPager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}