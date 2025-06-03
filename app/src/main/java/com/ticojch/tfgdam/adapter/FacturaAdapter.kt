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

class FacturaAdapter(private val productos: List<ProductoCarrito>) :
    RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder>() {

    class FacturaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.name_product)
        val cantidad: TextView = view.findViewById(R.id.cant_product)
        val precio: TextView = view.findViewById(R.id.price_product)
        val total: TextView = view.findViewById(R.id.totalProducto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacturaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_factura, parent, false)
        return FacturaViewHolder(view)
    }

    override fun onBindViewHolder(holder: FacturaViewHolder, position: Int) {
        val item = productos[position]
        holder.nombre.text = item.nombre
        holder.cantidad.text = "Cant: ${item.cantidad}"
        holder.total.text = "${String.format("%.2f", item.precio * item.cantidad)} €"
        holder.precio.text = "${item.precio} €"
    }

    override fun getItemCount(): Int = productos.size
}
