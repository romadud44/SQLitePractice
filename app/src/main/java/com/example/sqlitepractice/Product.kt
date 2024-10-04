package com.example.sqlitepractice

data class Product(
    val productId: Int,
    val productName: String,
    val productWeight: Int,
    val productPrice: Int
) {
    companion object {
        var products: MutableList<Product> = mutableListOf()

    }
}