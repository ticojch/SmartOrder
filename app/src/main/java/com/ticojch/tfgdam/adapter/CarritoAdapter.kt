package com.ticojch.tfgdam.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ticojch.tfgdam.ProductoCarrito
import com.ticojch.tfgdam.R
import com.ticojch.tfgdam.databinding.FragmentCarritoBinding
import com.ticojch.tfgdam.databinding.ItemProductBinding

class CarritoAdapter(private val productos: MutableList<ProductoCarrito>) :
    RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    class CarritoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre = view.findViewById<TextView>(R.id.name_product)
        val cantidad = view.findViewById<TextView>(R.id.cant_product)
        val precio = view.findViewById<TextView>(R.id.price_product)
        val total = view.findViewById<TextView>(R.id.totalProducto)
        val imagen: ImageView = view.findViewById(R.id.img_product) // Aquí agregas tu ImageView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return CarritoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val item = productos[position]
        holder.nombre.text = item.nombre
        holder.cantidad.text = "Cantidad: ${item.cantidad}"
        holder.total.text = "Total: ${item.precio * item.cantidad} €"
        holder.precio.text = "${item.precio} +€"
        Glide.with(holder.itemView.context).load(item.imgUrl).into(holder.imagen)
    }

    override fun getItemCount(): Int = productos.size
}
