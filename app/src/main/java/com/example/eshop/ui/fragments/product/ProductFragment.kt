package com.example.eshop.ui.fragments.product

import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.eshop.R
import com.example.eshop.databinding.FragmentProductBinding
import com.example.eshop.data.remote.ResultWrapper
import com.example.eshop.data.remote.model.*
import com.example.eshop.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment : Fragment(R.layout.fragment_product) {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProductViewModel>()
    private val args by navArgs<ProductFragmentArgs>()
    private lateinit var imageViewPagerAdapter: ImageViewPagerAdapter
    private lateinit var relatedProductsAdapter: RelatedProductsAdapter
    private var customerId: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DataBindingUtil.bind(view)

        getCustomerId()
        initRecyclerView()
        getProduct()
        setUpViewPager()
        getOrder()
        checkIsToCard()
    }

    private fun initRecyclerView() {
        relatedProductsAdapter = RelatedProductsAdapter()
        binding.relatedRv.adapter = relatedProductsAdapter
    }

    private fun relatedProducts(list: Array<Int>) {
        viewModel.getProductsById(list)
        viewModel.getProducts.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Success -> {
                    relatedProductsAdapter.submitList(it.data)
                }
                is ResultWrapper.Loading -> {}
                is ResultWrapper.Error -> {}
            }
        }
    }

    private fun getCustomerId() {
        viewModel.pref.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            customerId = it.id
        }
    }

    private fun checkIsToCard() {
        viewModel.pref.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            viewModel.getOrders(it.id, "pending")
        }
        viewModel.getOrderList.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Success -> {
                    hasCardVisibility(it.data)
                }
            }
        }
    }

    private fun hasCardVisibility(data: List<Order>) = binding.apply {
        if (data.isNotEmpty()) {
            val productIds = Mapper.transformLineItemToProductsId(data[0].lineItems)
            if (productIds.contains(args.productId)) {
                basketBtn.gone()
                cardViewCounter.gone()
                cardViewHasCard.visible()
                goToCardBtn.setOnClickListener {
                    val action = ProductFragmentDirections.actionProductFragmentToHomeCartFragment()
                    findNavController().navigate(action)
                }
            } else {
                basketBtn.visible()
                cardViewCounter.gone()
                cardViewHasCard.gone()
            }
        }
    }

    private fun checkStatusOrder(data: Product) {
        binding.basketBtn.setOnClickListener {
            viewModel.getOrders(customerId, "pending")
            //launchIn()
            viewModel.getOrderList.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
                when (it) {
                    is ResultWrapper.Success -> {
                        if (it.data.isEmpty()) {
                            createNewOrder(data)
                        } else {
                            updateOrder(data, it.data)
                        }
                    }
                }
            }
        }
    }

    private fun getOrder() {
        viewModel.getOrder.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
//            if (it.isSuccess()) { }
            when (it) {
                is ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
                    visibilityAddToCartBtn(it)
                    inc(it.data)
                    dec(it.data)
                }
                is ResultWrapper.Error -> {}
            }
        }
    }

    private fun visibilityAddToCartBtn(it: ResultWrapper.Success<Order>) {
        val productIdItem = Mapper.transformLineItemToProductsId(it.data.lineItems)
        if (productIdItem.contains(args.productId)) {
            binding.basketBtn.gone()
            binding.cardViewCounter.visible()
        } else {
            binding.basketBtn.visible()
            binding.cardViewCounter.gone()
        }
    }

    private fun inc(orderItem: Order) = binding.apply {
        if (orderItem.lineItems.isEmpty()) {
            binding.basketBtn.visible()
            binding.cardViewCounter.gone()
        } else {
            counter.text = orderItem.lineItems.last().quantity.toString()
            plus.setOnClickListener {
                val quantity = orderItem.lineItems.last().quantity
                val lineItemList = mutableListOf<LineItem>()
                val lineItem =
                    LineItem(
                        orderItem.lineItems.last().id,
                        orderItem.lineItems.last().name,
                        orderItem.lineItems.last().productId,
                        quantity + 1,
                        emptyList(),
                        0.0
                    )
                lineItemList.add(lineItem)
                val order = SetOrder(
                    orderItem.id,
                    customerId,
                    lineItemList,
                    "pending",
                    emptyList()
                )
                viewModel.updateOrder(orderItem.id, order)
            }
        }
    }

    private fun dec(orderItem: Order) = binding.apply {
        if (orderItem.lineItems.isEmpty()) {
            binding.basketBtn.visible()
            binding.cardViewCounter.gone()
        } else {
            counter.text = orderItem.lineItems.last().quantity.toString()
            if (orderItem.lineItems.last().quantity == 1) {
                minus.setIconResource(R.drawable.ic_baseline_delete)
            } else minus.setIconResource(R.drawable.ic_baseline_remove)

            minus.setOnClickListener {
                val quantity = orderItem.lineItems.last().quantity
                val lineItemList = mutableListOf<LineItem>()
                val lineItem =
                    LineItem(
                        orderItem.lineItems.last().id,
                        orderItem.lineItems.last().name,
                        orderItem.lineItems.last().productId,
                        quantity - 1,
                        emptyList(),
                        0.0
                    )
                lineItemList.add(lineItem)
                val order = SetOrder(
                    orderItem.id,
                    customerId,
                    lineItemList,
                    "pending",
                    emptyList()
                )
                viewModel.updateOrder(orderItem.id, order)

                if (quantity <= 0) {
                    deleteProductFromOrder(orderItem)
                } else {
                    viewModel.updateOrder(orderItem.id, order)
                }
            }
        }
    }

    private fun deleteProductFromOrder(orderItems: Order) {
        val lineItemList = mutableListOf<LineItem>()
        val lineItem = LineItem(
            orderItems.lineItems.last().id,
            "",
            orderItems.lineItems.last().productId,
            0,
            emptyList(),
            0.0
        )

        lineItemList.add(lineItem)
        val order = SetOrder(
            orderItems.id,
            customerId,
            lineItemList,
            "pending",
            emptyList()
        )
        viewModel.updateOrder(orderItems.id, order)
    }

    private fun updateOrder(data: Product, orderId: List<Order>) {
        val lineItemList = mutableListOf<LineItem>()
        val metaData = mutableListOf<MetaData>()
        val metaDataItem = MetaData(0, "image", data.images[0].src)
        metaData.add(metaDataItem)
        val lineItem = LineItem(0, data.name, data.id, 1, metaData, 0.0)
        lineItemList.add(lineItem)
        val order = SetOrder(
            orderId[0].id,
            customerId,
            lineItemList,
            "pending",
            emptyList()
        )
        viewModel.updateOrder(orderId[0].id, order)
    }

    private fun createNewOrder(data: Product) {
        val lineItemList = mutableListOf<LineItem>()
        val metaData = mutableListOf<MetaData>()
        val metaDataItem = MetaData(0, "image", data.images[0].src)
        metaData.add(metaDataItem)
        val lineItem = LineItem(0, data.name, data.id, 1, metaData, 0.0)
        lineItemList.add(lineItem)
        val order =
            SetOrder(0, customerId, lineItemList, "pending", emptyList())
        viewModel.setOrder(order)
    }

    private fun goToReviews(productId: Int) {
        binding.reviewsBtn.setOnClickListener {
            val action = ProductFragmentDirections.actionProductFragmentToReviewsFragment(productId)
            findNavController().navigate(action)
        }
    }

    private fun getProduct() = binding.apply {
        viewModel.getProduct(args.productId)
        viewModel.getProduct.collectWithRepeatOnLifecycle(viewLifecycleOwner) {
            when (it) {
                is ResultWrapper.Error -> {
                    isError()
                }
                is ResultWrapper.Loading -> {
                    isLoading()
                }
                is ResultWrapper.Success -> {
                    isSuccess(it.data)
                    checkStatusOrder(it.data)
                    goToReviews(it.data.id)
                    relatedProducts(it.data.relatedIds.toTypedArray())
                }
            }
        }
    }

    private fun isLoading() = binding.apply {
        loading.visible()
        productGp.gone()
        loading.playAnimation()
    }

    private fun isError() = binding.apply {
        loading.visible()
        productGp.gone()
        loading.playAnimation()
        requireView().snackBar("دریافت اطلاعات با مشکل مواجه شد")
    }

    private fun isSuccess(data: Product) = binding.apply {
        loading.gone()
        productGp.visible()
        loading.pauseAnimation()
        productDetails = data
        productRating.text = "(${data.ratingCount}) ${data.averageRating}"
        ratingBar.rating = data.averageRating.toFloat()
        productDec.text = data.let { product ->
            HtmlCompat.fromHtml(product.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
        imageViewPagerAdapter.submitList(data.images)
    }

    private fun setUpViewPager() {
        imageViewPagerAdapter = ImageViewPagerAdapter()
        binding.viewPager.adapter = imageViewPagerAdapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.wormDotsIndicator.attachTo(binding.viewPager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.relatedRv.adapter = null
        _binding = null
    }
}