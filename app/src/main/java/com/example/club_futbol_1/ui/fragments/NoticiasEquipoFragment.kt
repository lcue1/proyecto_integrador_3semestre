package com.example.club_futbol_1.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
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
    private var esAdmin:Boolean=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {// obtiene usuario
            esAdmin = it.getBoolean("esAdmin")
        }
    }

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
        Log.d("esAdmin",esAdmin.toString())
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

                val customAdapter = NoticiasAdapter(
                    esAdministrador = esAdmin,
                    noticias = noticias,
                    editarNoticia = {noticiaEditar->
                        val bundle = Bundle().apply {
                            putParcelable("noticiaEditar", noticiaEditar)
                        }

                        findNavController().navigate(R.id.action_noticiasEquipoFragment_to_addNoticiaFragment, bundle)
                    },
                    eliminarNoticia = {idNoticia->
                        val builder =AlertDialog.Builder(context)
                        builder
                            .setMessage("Desea eliminar esta noticia?")
                            .setTitle("Eliminar")
                            .setPositiveButton("Si"){dialog,wich->
                                eliminarNoticiaFirebase(idNoticia)
                            }
                            .setNegativeButton("No",null)


                        val dialog: AlertDialog = builder.create()
                        dialog.show()//mestra el dialogo
                    }
                )

                val recyclerView: RecyclerView = binding.noticiasRecycle
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = customAdapter
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error al obtener documentos.", exception)
            }
    }

    private fun eliminarNoticiaFirebase(idNoticia: String) {
        val db =FirebaseFirestore.getInstance()
        db.collection("noticias")
            .document(idNoticia)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(requireContext(),"Noticia eliminada",Toast.LENGTH_LONG)
                val bundle =Bundle().apply {
                    putBoolean("esAdmin",true)
                }

                findNavController().navigate(R.id.noticiasEquipoFragment, bundle)

            }
            .addOnFailureListener { e->
                Log.d("editarfirebase","$e")
            }

    }
}