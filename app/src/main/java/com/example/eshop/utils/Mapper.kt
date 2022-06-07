package com.example.eshop.utils

import com.example.eshop.data.local.model.LocalProduct
import com.example.eshop.data.remote.model.LineItem
import com.example.eshop.data.remote.model.Product

object Mapper {

    fun transformRemoteProductToLocalProduct(remoteProduct: Product): LocalProduct {
        return LocalProduct(
            id = remoteProduct.id,
            name = remoteProduct.name,
            price = remoteProduct.price,
            images = remoteProduct.images[0].src,
            quantity = 1
        )
    }

    fun transformProductsToLineItem(products: List<LocalProduct>)
            : List<LineItem> {
        return products.map {
            LineItem(it.id, it.quantity)
        }
    }

}