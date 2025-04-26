package com.ticojch.tfgdam

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.content.SharedPreferences
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import com.ticojch.tfgdam.adapter.ProductAdapter
import com.ticojch.tfgdam.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Context
import android.widget.EditText
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {

    var mesaId : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main)

        configurarMesa(this)
        val toolbar = findViewById<Toolbar>(R.id.miToolbar)
        setSupportActionBar(toolbar)

        verMenu()
    }

    fun verCarrito(){
        Log.i("syso","Abriendo carrito")
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_menu, CarritoFragment()) // ID del FrameLayout que contiene los fragments
            .addToBackStack(null) // Para que puedas volver atrás con el botón "atrás"
            .commit()
    }

    fun verMenu(){
        Log.i("syso","Iniciando el menu")
        val fragment = fragment_menu()
        val bundle = Bundle()
        bundle.putString("mesaId",mesaId)
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_menu, fragment).commit()
    }

    fun configurarMesa(context: Context){
        Log.i("syso","Configurando mesa")

        val conf = context.getSharedPreferences("Parameters", Context.MODE_PRIVATE)
        mesaId = conf.getString("mesa_id",null)

        val editText = EditText(this)
        editText.hint = "formato: mesa_01 "
        editText.setTextColor(resources.getColor(android.R.color.black))
        editText.setHintTextColor(resources.getColor(android.R.color.darker_gray))
        editText.setBackgroundResource(android.R.color.white)

        if(mesaId == null){
            val dialog = AlertDialog.Builder(this, com.google.android.material.R.style.Theme_MaterialComponents_DayNight_Dialog_Alert)
                .setTitle("Bienvenido")
                .setMessage("Por favor, introduce el id de la mesa: mesa_01")
                .setView(editText)
                .setPositiveButton("Guardar") { dialogInterface, _ ->
                    val mesaIngresada = editText.text.toString().trim()
                    if (mesaIngresada.isNotEmpty()) {
                        conf.edit().putString("mesa_id", mesaIngresada).apply()
                    }
                    dialogInterface.dismiss()
                }
                .setNegativeButton("Cancelar") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .setCancelable(false) // No permitir cerrar el diálogo tocando fuera
                .create()

            dialog.show()
        }else{
            Toast.makeText(context,"Mesa configurada: " + mesaId,Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // Acción para llamar al camarero
                Toast.makeText(this, "¡Camarero en camino!", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.button_carrito -> {
                // Acción para abrir el carrito
                verCarrito()
                true
            }
            R.id.button_inicio ->{
                verMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}