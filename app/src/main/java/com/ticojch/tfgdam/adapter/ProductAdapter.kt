package com.ticojch.tfgdam.adapter

import android.content.DialogInterface.OnClickListener
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ticojch.tfgdam.Producto
import com.ticojch.tfgdam.R

class ProductAdapter(val products:List<Producto>, private val onClickListener:(Producto)->Unit): RecyclerView.Adapter<ProductViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductViewHolder(layoutInflater.inflate(R.layout.item_product, parent, false))
        Log.i("syso","CreateViewHolder")
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = products[position]
        Log.i("syso","BindViewHolder")
        holder.render(item, onClickListener)
    }

    override fun getItemCount() = products.size

}
