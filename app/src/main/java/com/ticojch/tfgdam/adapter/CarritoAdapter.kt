package com.ticojch.tfgdam.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.ticojch.tfgdam.ProductoCarrito
import com.ticojch.tfgdam.R
import com.ticojch.tfgdam.databinding.FragmentCarritoBinding
import com.ticojch.tfgdam.databinding.ItemProductBinding

class CarritoAdapter(private val productos: MutableList<ProductoCarrito>, private val deleteButton: (ProductoCarrito) -> Unit) :
    RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    class CarritoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre = view.findViewById<TextView>(R.id.name_product)
        val cantidad = view.findViewById<TextView>(R.id.cant_product)
        val precio = view.findViewById<TextView>(R.id.price_product)
        val total = view.findViewById<TextView>(R.id.totalProducto)
        val imagen: ImageView = view.findViewById(R.id.img_product)
        val eliminar_btn  = view.findViewById<MaterialButton>(R.id.delete_product)
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
        holder.total.text = "Total: ${String.format("%.2f", item.precio * item.cantidad)}"
        holder.precio.text = "${item.precio} €"
        Glide.with(holder.itemView.context).load(item.imgUrl).into(holder.imagen)
        holder.eliminar_btn.setOnClickListener {
            deleteButton(item) // cuando presiones el botón, ejecuta esta función
        }
    }

    override fun getItemCount(): Int = productos.size
}
