package com.ticojch.tfgdam

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ticojch.tfgdam.adapter.ProductAdapter
import com.ticojch.tfgdam.databinding.FragmentMenuBinding


class fragment_menu() : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private var mesaId :String? = null
    val db = Firebase.firestore
    val listaProductos = mutableListOf<Producto>()
    private val productosSeleccionados : MutableList<ProductoCarrito> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        mesaId = arguments?.getString("mesaId")
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

        binding.recyclerView.layoutManager = manager
//        binding.recyclerView.adapter = ProductAdapter(productos, {onItemSelected(it)})
        Log.i("syso","Obteniendo base de datos")
        db.collection("platos").get()
            .addOnSuccessListener { result ->
                listaProductos.clear()
                Log.i("syso",result.toString())
                Log.d("syso", "Cantidad de documentos: ${result.size()}")
                for (document in result) {
                    val plato = document.toObject(Producto::class.java)
                    if(plato.disponible){
                        listaProductos.add(plato)
                    }
                }
                binding.recyclerView.adapter = ProductAdapter(listaProductos,productosSeleccionados) {
                    for (producto in productosSeleccionados) {
                        val productoMap = mapOf(
                            "nombre" to producto.nombre,
                            "precio" to producto.precio,
                            "cantidad" to producto.cantidad,
                            "imgUrl" to producto.imgUrl
                        )
                        db.collection("mesas")
                            .document(mesaId.toString())
                            .collection("productosToSend")
                            .add(productoMap)
                            .addOnSuccessListener { documentReference ->
                                Toast.makeText(
                                    requireContext(),
                                    "Producto agregado a mesa correctamente",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    requireContext(),
                                    "Error al agregar producto: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
            }
            .addOnFailureListener {
                Log.i("syso","Error al cargar base de datos")
                Toast.makeText(context, "Error cargando el menú", Toast.LENGTH_SHORT).show()
            }

        binding.recyclerView.addItemDecoration(decoration)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}