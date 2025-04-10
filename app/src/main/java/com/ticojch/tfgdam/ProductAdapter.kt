package com.ticojch.tfgdam

import android.view.LayoutInflater
import android.view.View;
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView;

class ProductAdapter(val products:List<Producto>): RecyclerView.Adapter<ProductAdapter.ProductHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductHolder(layoutInflater.inflate(R.layout.item_product, parent, false))
    }

    override fun onBindViewHolder(
        holder: ProductHolder,
        position: Int
    ) {
        holder.render(products[position])
    }

    override fun getItemCount() = products.size



    class ProductHolder(val view:View): RecyclerView.ViewHolder(view){
        fun render(products: Producto){
            view.name_product.text = products.nombre

        }
    }
}
