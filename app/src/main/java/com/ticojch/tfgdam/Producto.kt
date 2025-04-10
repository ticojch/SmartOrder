package com.ticojch.tfgdam

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val url_img: String,
    val id_categoria: Int)

/**
 * id_categoria:
 * 0 - Entrantes
 * 1 - Arroces
 * 2 - Pollos
 * 3 - Bebidas
 * **/