package com.ticojch.tfgdam

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

    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCarritoBinding.inflate(inflater, container, false)
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

        binding.recyclerViewCarrito.layoutManager = manager
//        binding.recyclerView.adapter = ProductAdapter(productos, {onItemSelected(it)})
        Log.i("syso","Obteniendo base de datos")
        db.collection("mesas").document(mesaId.toString()).collection("productosToSend").get()
            .addOnSuccessListener { result ->
                productosSeleccionados.clear()
                val platosToSend = mutableListOf<ProductoCarrito>()
                Log.i("syso",result.toString())
                Log.d("syso", "Cantidad de documentos: ${result.size()}")
                for (document in result) {
                    val plato = document.toObject(ProductoCarrito::class.java)
                    if(plato.cantidad>0){
                        platosToSend.add(plato)
                    }
                }
                for (plato in platosToSend) {
                    Log.i("syso", "Producto: ${plato.nombre}, Cantidad: ${plato.cantidad}")
                }
                binding.recyclerViewCarrito.adapter = CarritoAdapter(platosToSend)

            }
            .addOnFailureListener {
                Log.i("syso","Error al cargar base de datos")
                Toast.makeText(context, "Error cargando el menú", Toast.LENGTH_SHORT).show()
            }
        binding.recyclerViewCarrito.addItemDecoration(decoration)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}