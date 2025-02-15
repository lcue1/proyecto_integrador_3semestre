package com.example.club_futbol_1.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.club_futbol_1.R
import com.example.club_futbol_1.databinding.FragmentNoticiasEquipoBinding
import com.example.club_futbol_1.databinding.FragmentTiendaBinding
import com.example.club_futbol_1.model.Noticia
import com.example.club_futbol_1.model.Producto
import com.example.club_futbol_1.ui.adapters.NoticiasAdapter
import com.example.club_futbol_1.ui.adapters.TiendaAdapter
import com.google.firebase.firestore.FirebaseFirestore


class TiendaFragment : Fragment() {
    private var _binding:FragmentTiendaBinding?=null
    private val binding get()= _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cargarProductos()
        _binding = FragmentTiendaBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun cargarProductos() {
        val db = FirebaseFirestore.getInstance()

        db.collection("tienda")
            .get()
            .addOnSuccessListener { result ->
                val productos= mutableListOf<Producto>()
                for (document in result) {
                    // Accede a los datos del documento

                    val producto=Producto(
                        id_document=document.id,
                        precio_producto = document.getDouble("precio_producto")?:0.0,
                        url_img_producto = document.getString("url_img_producto")?:"",
                        nombre_producto = document.getString("nombre_producto")?:"",
                        descripcion_producto = document.getString("descripcion_producto")?:""
                    )
                    Log.d("producto",producto.toString())
                    productos.add(producto)
                    //add coleccion productos
                }

                val customAdapter = TiendaAdapter(productos)
                val recyclerView: RecyclerView = binding.noticiasRecycle
                recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
                recyclerView.adapter = customAdapter

            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error al obtener documentos.", exception)
            }
    }

}