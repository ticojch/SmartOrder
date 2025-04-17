package com.ticojch.tfgdam

data class Producto(
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val imgUrl: String = "",
    val disponible: Boolean = true)

/**
 * id_categoria:
 * 0 - Entrantes
 * 1 - Arroces
 * 2 - Pollos
 * 3 - Bebidas
 * **/