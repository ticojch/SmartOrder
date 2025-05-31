package com.ticojch.tfgdam.adapter

import com.ticojch.tfgdam.R
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.ticojch.tfgdam.Producto
import com.ticojch.tfgdam.databinding.ItemProductBinding
import android.widget.Toast



class ProductViewHolder(val view: View):RecyclerView.ViewHolder(view) {

    val binding = ItemProductBinding.bind(view)
    private val cantidadText = view.findViewById<TextView>(R.id.cant_product)
    private val btnMas = view.findViewById<MaterialButton>(R.id.add_product_btn)
    private val btnMenos = view.findViewById<Button>(R.id.sub_product_btn)
    private val btnAdd = view.findViewById<Button>(R.id.sendToCar_btn)

//    var cantidad = 0

    fun render(product: Producto,  onAgregarProducto: (Producto, Int) -> Unit){
        Log.i("render","Render producto")

        binding.nameProduct.text = product.nombre
        binding.descripcionProduct.text = product.descripcion
        binding.priceProduct.text = product.precio.toString()
        Glide.with(binding.imgProduct.context).load(product.imgUrl).into(binding.imgProduct)

        cantidadText.text = product.cantidad.toString()

        Log.i("render","Render cantidad ${product.cantidad.toString()}")
        btnMas.setOnClickListener {
            Log.i("render","Aumentando cantidad")
            Toast.makeText(view.context, "Click en +", Toast.LENGTH_SHORT).show()
            product.cantidad++
            cantidadText.text = product.cantidad.toString()
//            onAgregarProducto(product, cantidad)
        }

        btnMenos.setOnClickListener {
            if (product.cantidad > 0) {
                product.cantidad--
                cantidadText.text = product.cantidad.toString()
//                onAgregarProducto(product, cantidad)
            }
        }

        btnAdd.setOnClickListener {
            if (product.cantidad > 0) {
                Log.i("render","AGREGADO AL CARITO")
                onAgregarProducto(product, product.cantidad)
                cantidadText.text = product.cantidad.toString()
            }else{
                Log.i("render","Agrega una cantidad")
            }
        }



    }
}