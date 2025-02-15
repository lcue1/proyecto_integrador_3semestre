package com.example.club_futbol_1.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.club_futbol_1.R
import com.example.club_futbol_1.databinding.FragmentInfoUssuarioBinding
import com.example.club_futbol_1.databinding.FragmentNoticiasEquipoBinding
import com.example.club_futbol_1.model.Noticia
import com.example.club_futbol_1.ui.adapters.NoticiasAdapter
import com.google.firebase.firestore.FirebaseFirestore


class NoticiasEquipoFragment : Fragment() {

    private var _binding: FragmentNoticiasEquipoBinding? = null
    private val binding get() = _binding!! // Acceso seguro a binding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cargarNoticias()
        _binding = FragmentNoticiasEquipoBinding.inflate(inflater, container, false)
        return binding.root
    }



    private fun cargarNoticias() {
        val db = FirebaseFirestore.getInstance()

        db.collection("noticias")
            .get()
            .addOnSuccessListener { result ->
                val noticias= mutableListOf<Noticia>()
                for (document in result) {
                    // Accede a los datos del documento
                    val idDocument = document.id// Campo
                    val titulo = document.getString("titulo")  // Campo
                    val descripcion = document.getString("descripcion")
                    val descripcionCorta = descripcion!!.take(200)
                    val utlImg = document.getString("urlImg")  // C
                    noticias.add(Noticia(
                        id = idDocument,
                        titulo = titulo,
                        descripcion=descripcionCorta,
                        urlImagen = utlImg
                    ))
                }
                Log.d("noticias",noticias.toString())

                val customAdapter = NoticiasAdapter(noticias)

                val recyclerView: RecyclerView = binding.noticiasRecycle
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = customAdapter
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error al obtener documentos.", exception)
            }
    }
}