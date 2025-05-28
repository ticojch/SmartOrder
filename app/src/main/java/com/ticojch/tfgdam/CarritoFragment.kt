package com.ticojch.tfgdam

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ticojch.tfgdam.adapter.CarritoAdapter
import com.ticojch.tfgdam.adapter.ProductAdapter
import com.ticojch.tfgdam.databinding.FragmentCarritoBinding

class CarritoFragment : Fragment() {
    private var _binding: FragmentCarritoBinding? = null
    private val binding get() = _binding!!

    private val productosSeleccionados : MutableList<ProductoCarrito> = mutableListOf()
    private var mesaId :String? = null
    private var totalAPagar = 0.0

    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCarritoBinding.inflate(inflater, container, false)
        val conf = context?.getSharedPreferences("Parameters", Context.MODE_PRIVATE)
        mesaId = conf?.getString("mesa_id",null)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    fun initRecyclerView(){
        val context = requireContext()
        val manager = LinearLayoutManager(context)
        val decoration = DividerItemDecoration(context, manager.orientation)

        binding.recyclerViewCarrito.layoutManager = manager
        Log.i("syso","Obteniendo base de datos: "+mesaId.toString())
        db.collection("mesas").document(mesaId.toString()).collection("productosToSend").get()
            .addOnSuccessListener { result ->
                productosSeleccionados.clear()
                Log.i("syso",result.toString())
                for (document in result) {
                    val plato = document.toObject(ProductoCarrito::class.java)
                    if(plato.cantidad>0){
                        productosSeleccionados.add(plato)
                    }
                }
                for (plato in productosSeleccionados) {
                    Log.i("syso", "Producto: ${plato.nombre}, Cantidad: ${plato.cantidad}")
                }
                totalAPagar()
                binding.recyclerViewCarrito.adapter = CarritoAdapter(productosSeleccionados) { producto ->
                    //Eliminar el producto del carrito al presionar el boton
                    db.collection("mesas")
                        .document(mesaId.toString())
                        .collection("productosToSend")
                        .document(producto.nombre)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "${producto.nombre} eliminado del carrito", Toast.LENGTH_SHORT).show()
                            initRecyclerView()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Error eliminando producto: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Log.i("syso","Error al cargar base de datos")
                Toast.makeText(context, "Error cargando el menú", Toast.LENGTH_SHORT).show()
            }
        binding.recyclerViewCarrito.addItemDecoration(decoration)
    }

    fun sendProductsToCocinar(){

    }

    fun totalAPagar(){
        totalAPagar = 0.0
        for (product in productosSeleccionados) {
            this.totalAPagar += (product.precio*product.cantidad)
        }
        binding.totalAPagar.text = "${this.totalAPagar}€"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}