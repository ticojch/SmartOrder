package com.ticojch.tfgdam

import android.content.Context
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


class MenuFragment() : Fragment() {
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

        binding.recyclerView.layoutManager = manager
        Log.i("syso","Obteniendo base de datos")
       cargarMenu()

        binding.recyclerView.addItemDecoration(decoration)
    }

    fun cargarMenu() {
        db.collection("platos").get()
            .addOnSuccessListener { result ->
                listaProductos.clear()
                Log.i("syso", result.toString())

                val platosDisponibles = result.filter { it.toObject(Producto::class.java).disponible }
                var platosProcesados = 0

                if (platosDisponibles.isEmpty()) {
                    binding.recyclerView.adapter = ProductAdapter(listaProductos, productosSeleccionados) { /* vacío */ }
                    return@addOnSuccessListener
                }

                for (document in platosDisponibles) {
                    val plato = document.toObject(Producto::class.java)

                    devolverCantidadProductoSeleccionado(plato.nombre.toString()) { cantidad ->
                        plato.cantidad = cantidad
                        listaProductos.add(plato)
                        platosProcesados++

                        if (platosProcesados == platosDisponibles.size) {
                            _binding?.let {
                                it.recyclerView.adapter = ProductAdapter(listaProductos, productosSeleccionados) {
                                    for (producto in productosSeleccionados) {
                                        val productoMap = mapOf(
                                            "nombre" to producto.nombre,
                                            "precio" to producto.precio,
                                            "cantidad" to producto.cantidad.toLong(),
                                            "imgUrl" to producto.imgUrl,
                                            "estado" to 0
                                        )

                                        val productosRef = db.collection("mesas")
                                            .document(mesaId.toString())
                                            .collection("productosToSend")
                                            .document(producto.nombre)

                                        productosRef.get()
                                            .addOnSuccessListener { doc ->
                                                if (doc.exists()) {
                                                    productosRef.update("cantidad", producto.cantidad)
                                                } else {
                                                    productosRef.set(productoMap)
                                                }
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
                        }
                    }
                }
            }
            .addOnFailureListener {
                Log.i("syso", "Error al cargar base de datos")
                Toast.makeText(context, "Error cargando el menú", Toast.LENGTH_SHORT).show()
            }
    }

//    //Verificar que el producto ya haya sido seleccionado
    fun devolverCantidadProductoSeleccionado(nombrePlato:String, callback:(Int) -> Unit){
        var cantidad:Int = 0
        val docRef = db.collection("mesas").document(mesaId.toString()).collection("productosToSend").document(nombrePlato)
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    cantidad = (documentSnapshot.getLong("cantidad") ?: 0).toInt()
                    Log.d("Firebase", "Cantidad: $cantidad")
                    callback(cantidad)
                }
                else {
                    callback(0)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error al consultar: ${e.message}")
                callback(0)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}