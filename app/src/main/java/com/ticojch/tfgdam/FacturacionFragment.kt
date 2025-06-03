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
import com.ticojch.tfgdam.adapter.FacturaAdapter
import com.ticojch.tfgdam.databinding.FragmentFacturacionBinding

class FacturacionFragment : Fragment() {
    private var _binding: FragmentFacturacionBinding? = null
    private val binding get() = _binding!!
    private var mesaId :String? = null
    val db = Firebase.firestore
    val listaProductos = mutableListOf<ProductoCarrito>()
    var totalAPagar = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFacturacionBinding.inflate(inflater, container, false)
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

        binding.recyclerViewProductos.layoutManager = manager
        Log.i("syso","Obteniendo base de datos")
        cargarMenu()

        binding.recyclerViewProductos.addItemDecoration(decoration)
    }

    fun cargarMenu() {
        db.collection("mesas").document(mesaId.toString()).collection("productosConfirmed").get()
            .addOnSuccessListener { result ->
                listaProductos.clear()
                Log.i("syso", result.toString())

                for (document in result) {
                    val producto = document.toObject(ProductoCarrito::class.java)
                    listaProductos.add(producto)
                }
                totalAPagar()
                _binding?.let {
                    binding.recyclerViewProductos.adapter = FacturaAdapter(listaProductos)
                }
            }
            .addOnFailureListener {
                Log.i("syso", "Error al cargar base de datos")
                Toast.makeText(context, "Error cargando el menú", Toast.LENGTH_SHORT).show()
            }
    }

    fun totalAPagar(){
        totalAPagar = 0.0
        for (product in listaProductos) {
            this.totalAPagar += (product.precio*product.cantidad)
        }
        binding.totalAPagar.text = String.format("%.2f",this.totalAPagar)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}