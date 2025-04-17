package com.ticojch.tfgdam

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import com.ticojch.tfgdam.adapter.ProductAdapter
import com.ticojch.tfgdam.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    val db = Firebase.firestore
    val listaProductos = mutableListOf<Producto>()
//    var productos: List<Producto> = listOf(
//        Producto(0,"Rollitos de primavera","2 Rollitos de primavera",2.0,"https://firebasestorage.googleapis.com/v0/b/smartorder-f0af8.firebasestorage.app/o/arrozEspecial.jpg?alt=media&token=c67e6966-a8f6-424e-b06b-44bb85529d43",0),
//        Producto(0,"Gyozas","6 Gyozas rellenas de cerdo",4.0,"https://carolinarice.com/wp-content/uploads/2019/05/MahChickenFrideRice-1024x914-1.jpeg",0),
//        Producto(0,"Tequeños","6 tequeños de queso",5.0,"https://carolinarice.com/wp-content/uploads/2019/05/MahChickenFrideRice-1024x914-1.jpeg",0),
//        Producto(0,"Arroz frito","Arroz frito al wok con pollo, jamon y huevo",10.4,"https://carolinarice.com/wp-content/uploads/2019/05/MahChickenFrideRice-1024x914-1.jpeg",1),
//        Producto(0,"Arroz frito especial","Arroz frito al wok, con gambas, cerdo, pollo, jamon y huevo",2.0,"https://carolinarice.com/wp-content/uploads/2019/05/MahChickenFrideRice-1024x914-1.jpeg",1),
//        Producto(0,"Pollo agridulce","Piezas de pollo frito con salsa agridulce",6.0,"https://carolinarice.com/wp-content/uploads/2019/05/MahChickenFrideRice-1024x914-1.jpeg",2),
//        Producto(0,"Pollo ajonjoli","Piezas de pollo frito con salsa ajonjoli",6.5,"https://carolinarice.com/wp-content/uploads/2019/05/MahChickenFrideRice-1024x914-1.jpeg",2),
//        Producto(0,"Pollo al limon","Piezas de pollo frito con salsa al limon",7.0,"https://carolinarice.com/wp-content/uploads/2019/05/MahChickenFrideRice-1024x914-1.jpeg",2),
//        Producto(0,"Cocacola","33cl Cocacola",3.0,"https://carolinarice.com/wp-content/uploads/2019/05/MahChickenFrideRice-1024x914-1.jpeg",3),
//        Producto(0,"Fanta naranja","33cl Fanta de naranja",3.0,"https://carolinarice.com/wp-content/uploads/2019/05/MahChickenFrideRice-1024x914-1.jpeg",3)
//
//    )

    private lateinit var binding: ActivityMainBinding

//    private lateinit var toolBar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.miToolbar)
        setSupportActionBar(toolbar)

        initRecyclerView()

        val buttonCarrito = findViewById<FloatingActionButton>(R.id.button_carrito)
        buttonCarrito.setOnClickListener {
            verCarrito()
        }

    }

    fun initRecyclerView(){
        val manager = LinearLayoutManager(this)
        val decoration = DividerItemDecoration(this, manager.orientation)


        binding.recyclerView.layoutManager = manager
//        binding.recyclerView.adapter = ProductAdapter(productos, {onItemSelected(it)})
        Log.i("syso","Obteniendo base de datos")
        db.collection("platos").get()
            .addOnSuccessListener { result ->
                Log.i("syso",result.toString())
                Log.d("syso", "Cantidad de documentos: ${result.size()}")
                for (document in result) {
                    val plato = document.toObject(Producto::class.java)
                    if(plato.disponible){
                        listaProductos.add(plato)
                    }
                }
                binding.recyclerView.adapter = ProductAdapter(listaProductos,{onItemSelected(it)})
            }
            .addOnFailureListener {
                Log.i("syso","Error al cargar base de datos")
                Toast.makeText(this, "Error cargando el menú", Toast.LENGTH_SHORT).show()
            }

        binding.recyclerView.addItemDecoration(decoration)
    }

    fun verCarrito(){
        Log.i("syso","Abriendo carrito")
        val fragment = carrito()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_carrito, fragment) // ID del FrameLayout que contiene los fragments
            .addToBackStack(null) // Para que puedas volver atrás con el botón "atrás"
            .commit()
    }

    fun onItemSelected(producto: Producto){
        Toast.makeText(this, producto.nombre, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
}