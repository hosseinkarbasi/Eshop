package com.example.eshop.utils

import com.example.eshop.data.local.model.LocalProduct
import com.example.eshop.data.remote.model.LineItem
import com.example.eshop.data.remote.model.Product

object Mapper {

    fun transformRemoteProductToLocalProduct(
        remoteProduct: List<Product>,
        lineItems: List<LineItem>
    ): List<LocalProduct> {
        return remoteProduct.map {
            LocalProduct(
                id = it.id,
                name = it.name,
                price = it.price,
                images = it.images[0].src,
                lineItems
            )
        }
    }

//    fun transformProductsToLineItem(products: List<LocalProduct>)
//            : List<LineItem> {
//        return products.map {
//            LineItem(it.id, it.quantity)
//        }
//    }

    fun transformLineItemToProductsId(lineItem: List<LineItem>)
            : Array<Int> {
        return lineItem.map {
            it.productId
        }.toTypedArray()
    }

}