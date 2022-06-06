package com.example.eshop.ui.fragments.cart

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eshop.ui.fragments.cart.basket.BasketFragment
import com.example.eshop.ui.fragments.cart.orders.RegisteredOrdersFragment

class CartViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BasketFragment()
            1 -> RegisteredOrdersFragment()
            else -> BasketFragment()
        }
    }
}
