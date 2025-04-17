package com.ticojch.tfgdam.adapter

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ticojch.tfgdam.Producto
import com.ticojch.tfgdam.databinding.ItemProductBinding

class ProductViewHolder(val view: View):RecyclerView.ViewHolder(view) {

    val binding = ItemProductBinding.bind(view)

    fun render(product: Producto, onClickListener:(Producto)->Unit){
        Log.i("syso","Render producto")
        binding.nameProduct.text = product.nombre
        binding.descripcionProduct.text = product.descripcion
        binding.priceProduct.text = product.precio.toString()
        Glide.with(binding.imgProduct.context).load(product.imgUrl).into(binding.imgProduct)
        itemView.setOnClickListener{onClickListener(product)}
    }
}