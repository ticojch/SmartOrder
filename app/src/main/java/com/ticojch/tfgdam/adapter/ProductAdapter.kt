package com.ticojch.tfgdam.adapter

import android.content.DialogInterface.OnClickListener
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ticojch.tfgdam.Producto
import com.ticojch.tfgdam.ProductoCarrito
import com.ticojch.tfgdam.R

class ProductAdapter(val products:List<Producto>, private val productosSeleccionados: MutableList<ProductoCarrito>,
                     private val onTotalChanged: () -> Unit): RecyclerView.Adapter<ProductViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        Log.i("syso","CreateViewHolder")
        return ProductViewHolder(layoutInflater.inflate(R.layout.item_product, parent, false))
    }

//    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
//        val item = products[position]
//        Log.i("syso","BindViewHolder")
//        holder.render(item, onTotalChanged())
//    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = products[position]

        holder.render(item) { productoAgregado, cantidadSeleccionada ->

            val existente = productosSeleccionados.find { it.nombre == productoAgregado.nombre }

            if (existente != null) {
                Log.i("syso","Cantidad mod")
                existente.cantidad = cantidadSeleccionada
            } else {
                Log.i("syso","Producto agregado")
                    productosSeleccionados.add(ProductoCarrito(
                        nombre = productoAgregado.nombre,
                        imgUrl = productoAgregado.imgUrl,
                        precio = productoAgregado.precio,
                        cantidad = cantidadSeleccionada
                    ))
            }
            onTotalChanged()
        }
    }

    override fun getItemCount() = products.size

}
