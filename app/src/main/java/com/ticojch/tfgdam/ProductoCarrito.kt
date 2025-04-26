package com.ticojch.tfgdam

data class ProductoCarrito(
    val nombre: String = "",
    val precio: Double = 0.0,
    val imgUrl: String = "",
    var cantidad:Int=0
)
