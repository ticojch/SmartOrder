package com.ticojch.tfgdam

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    var productos: List<Producto> = listOf(
        Producto(0,"Rollitos de primavera","2 Rollitos de primavera",2.0,"https://img.com/253.png",0),
        Producto(0,"Gyozas","6 Gyozas rellenas de cerdo",4.0,"https://img.com/253.png",0),
        Producto(0,"Tequeños","6 tequeños de queso",5.0,"https://img.com/253.png",0),
        Producto(0,"Arroz frito","Arroz frito al wok con pollo, jamon y huevo",10.4,"https://img.com/253.png",1),
        Producto(0,"Arroz frito especial","Arroz frito al wok, con gambas, cerdo, pollo, jamon y huevo",2.0,"https://img.com/253.png",1),
        Producto(0,"Pollo agridulce","Piezas de pollo frito con salsa agridulce",6.0,"https://img.com/253.png",2),
        Producto(0,"Pollo ajonjoli","Piezas de pollo frito con salsa ajonjoli",6.5,"https://img.com/253.png",2),
        Producto(0,"Pollo al limon","Piezas de pollo frito con salsa al limon",7.0,"https://img.com/253.png",2),
        Producto(0,"Cocacola","33cl Cocacola",3.0,"https://img.com/253.png",3),
        Producto(0,"Fanta naranja","33cl Fanta de naranja",3.0,"https://img.com/253.png",3)

    );

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}