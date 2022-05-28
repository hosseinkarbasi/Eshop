package com.example.eshop.ui.fragments.product

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.eshop.R
import com.example.eshop.databinding.FragmentProductBinding
import com.example.eshop.utils.Result
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment : Fragment(R.layout.fragment_product) {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProductViewModel>()
    private val args by navArgs<ProductFragmentArgs>()
    private lateinit var imageViewPagerAdapter: ImageViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DataBindingUtil.bind(view)

        getProduct()
        setUpViewPager()
    }

    @SuppressLint("SetTextI18n")
    private fun getProduct() = binding.apply {
        viewModel.getProduct(args.productId)
        viewModel.getProduct.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {}
                is Result.Loading -> {}
                is Result.Success -> {
                    productDetails = it.data
                    productRating.text =
                        "(${it.data?.rating_count.toString()}) ${it.data?.average_rating}"
                    ratingBar.rating = it.data?.average_rating?.toFloat()!!
                    productDec.text = it.data.let { it1 ->
                        HtmlCompat.fromHtml(
                            it1.description,
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                    }
                    imageViewPagerAdapter.submitList(it.data.images)
                }
            }
        }
    }

    private fun setUpViewPager() {
        imageViewPagerAdapter = ImageViewPagerAdapter()
        binding.viewPager.adapter = imageViewPagerAdapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}