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
            images = remoteProduct.images[0].src
        )
    }

    fun transformProductsToLineItem(products: List<LocalProduct>, quantity: Int): List<LineItem> {
        return products.map {
            LineItem(it.id, quantity)
        }
    }

}