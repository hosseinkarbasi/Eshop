package com.example.eshop.ui.fragments.product

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.eshop.R
import com.example.eshop.data.remote.model.Product
import com.example.eshop.databinding.FragmentProductBinding
import com.example.eshop.utils.Mapper
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.utils.collectWithRepeatOnLifecycle
import com.example.eshop.utils.gone
import com.example.eshop.utils.visible
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

    private fun getProduct() = binding.apply {
        viewModel.getProduct(args.productId)
        viewModel.getProduct.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Error -> {
                    isError(it.message.toString())
                }
                is ResultWrapper.Loading -> {
                    isLoading()
                }
                is ResultWrapper.Success -> {
                    it.data?.let { it1 -> isSuccess(it1) }
                }
            }
        }
    }

    private fun isLoading() = binding.apply {
        loading.visible()
        headerCardView.gone()
        footerCardView.gone()
        productDecTv.gone()
        basketBtn.gone()
        loading.playAnimation()
    }

    private fun isError(errorMessage: String) = binding.apply {
        loading.visible()
        headerCardView.gone()
        footerCardView.gone()
        productDecTv.gone()
        basketBtn.gone()
        loading.playAnimation()
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    private fun isSuccess(data: Product) = binding.apply {
        loading.gone()
        headerCardView.visible()
        footerCardView.visible()
        productDecTv.visible()
        basketBtn.visible()
        loading.pauseAnimation()
        productDetails = data
        productRating.text = "(${data.ratingCount}) ${data.averageRating}"
        ratingBar.rating = data.averageRating.toFloat()
        productDec.text = data.let { product ->
            HtmlCompat.fromHtml(product.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
        imageViewPagerAdapter.submitList(data.images)
        basketBtn.setOnClickListener {
            viewModel.insertProduct(Mapper.transformRemoteProductToLocalProduct(data))
            Toast.makeText(requireContext(), "به سبد خرید  اضافه شد", Toast.LENGTH_SHORT).show()
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