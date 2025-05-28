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

                for (document in result) {
                    val plato = document.toObject(Producto::class.java)
                    if (plato.disponible) {
                        listaProductos.add(plato)
                    }
                }

                binding.recyclerView.adapter = ProductAdapter(listaProductos, productosSeleccionados) {
                    for (producto in productosSeleccionados) {
                        val productoMap = mapOf(
                            "nombre" to producto.nombre,
                            "precio" to producto.precio,
                            "cantidad" to producto.cantidad.toLong(),
                            "imgUrl" to producto.imgUrl,
                            "estado" to 0
                        )

                        val nombreProducto = producto.nombre
                        val productosRef = db.collection("mesas")
                            .document(mesaId.toString())
                            .collection("productosToSend")
                            .document(nombreProducto)

                        productosRef.get()
                            .addOnSuccessListener { doc ->
                                if (doc.exists()) {
                                    val nuevaCantidad =  producto.cantidad
                                    productosRef.update("cantidad", nuevaCantidad)
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
            .addOnFailureListener {
                Log.i("syso", "Error al cargar base de datos")
                Toast.makeText(context, "Error cargando el menú", Toast.LENGTH_SHORT).show()
            }
    }

//PRONTO A IMPLEMENTAR ALGORITMO PARA ACTUALIZAR EL MENU SEGUN LA CANTIDAD YA INGRESADA EN EL CARRITO
    //Cargar Menu desde la base de datos
//    fun cargarMenu(){
//        db.collection("platos").get()
//            .addOnSuccessListener { result ->
//                listaProductos.clear()
//                Log.i("syso",result.toString()) //Debug
//                for (document in result) {
//                    val plato = document.toObject(Producto::class.java)
//                    if(plato.disponible){
//                        listaProductos.add(plato)
//                    }
//                }
//                binding.recyclerView.adapter = ProductAdapter(listaProductos,productosSeleccionados) {
//                    for (producto in productosSeleccionados) {
//                        val productoMap = mapOf(
//                            "nombre" to producto.nombre,
//                            "precio" to producto.precio,
//                            "cantidad" to producto.cantidad,
//                            "imgUrl" to producto.imgUrl,
//                            "estado" to 0
//                        )
//                        addProductosBD(productoMap)
//                    }
//                }
//            }
//            .addOnFailureListener {
//                Log.i("syso","Error al cargar base de datos")
//                Toast.makeText(context, "Error cargando el menú", Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    //Agregar los platos seleccionados a la collection productosToSend de la mesa configurada
//    fun addProductosBD(nombreProduct:String):Int{
//        val cantidad = 0
//        db.collection("mesas")
//            .document(mesaId.toString())
//            .collection("productosToSend").document(nombreProduct.toString())
//            .get()
//            .addOnSuccessListener { documentReference ->
//                if(documentReference.exists()){
//                    cantidad = documentReference.data.cantidad
//                }else{
//                    return 0
//                }
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(
//                    requireContext(),
//                    "Error al consultar ${e.message}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//        return cantidad;
//    }
//
//    //Verificar que el producto ya haya sido seleccionado
//    fun verificarProductoSeleccionado(){
//            db.collection("mesas")
//                .document(mesaId.toString())
//                .collection("productosToSend").document(productosMenu["nombre"].toString())
//                .set(productosMenu)
//                .addOnSuccessListener { documentReference ->
//                    Toast.makeText(
//                        requireContext(),
//                        "Producto agregado a mesa correctamente",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//                .addOnFailureListener { e ->
//                    Toast.makeText(
//                        requireContext(),
//                        "Error al agregar producto: ${e.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}