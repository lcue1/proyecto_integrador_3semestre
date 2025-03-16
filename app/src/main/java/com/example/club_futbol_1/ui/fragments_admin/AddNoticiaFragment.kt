package com.example.club_futbol_1.ui.fragments_admin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.club_futbol_1.R
import com.example.club_futbol_1.Utils
import com.example.club_futbol_1.databinding.FragmentAddNoticiaBinding
import com.example.club_futbol_1.databinding.FragmentCarrritoBinding
import com.example.club_futbol_1.model.Noticia
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class AddNoticiaFragment : Fragment() {
    private var _binding: FragmentAddNoticiaBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentAddNoticiaBinding.inflate(inflater,container,false)
        val noticiaEditar = arguments?.getParcelable<Noticia>("noticiaEditar")

        val editTexts = arrayOf(binding.tituloAddNoticiaET,binding.descripcionNoticiaET,binding.urlNoticiaET)
        listenersEditEtexts(editTexts)
        if(noticiaEditar==null) {//Agrega la noticia
            binding.agregarEditarBtn.setOnClickListener {
                val campoVacio = Utils.validarEditText(editTexts)
                if(campoVacio==null){//todos los edit text  llenos
                    agregarNoticia()
                }
            }
        }else{//edita la noticia
            //cambio el disenio del boton y el texto
            binding.tituloAddNoticia.text = "Editar noticia: ${noticiaEditar.id}"
            binding.agregarEditarBtn.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.verde))
            binding.agregarEditarBtn.text = "Editar"
            binding.tituloAddNoticiaET.setText(noticiaEditar.titulo)
            binding.descripcionNoticiaET.setText(noticiaEditar.descripcion)
            binding.urlNoticiaET.setText(noticiaEditar.urlImagen)
            binding.agregarEditarBtn.setOnClickListener {
                val campoVacio = Utils.validarEditText(editTexts)
                if(campoVacio==null){//todos los edit text  llenos
                    editarNoticia(noticiaEditar.id!!)
                }
            }

        }


        return binding.root
    }

    private fun editarNoticia(documnentId:String) {
        val db =FirebaseFirestore.getInstance()
        val noticiaEditada = hashMapOf(
            "titulo" to binding.tituloAddNoticiaET.text.toString(),
            "descripcion" to binding.descripcionNoticiaET.text.toString(),
            "urlImg" to binding.urlNoticiaET.text.toString()
        )
        Log.d("noticiaactualizada",noticiaEditada.toString())
        db.collection("noticias").document(documnentId)
            .set(noticiaEditada, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(context, "Noticias actualizada.",Toast.LENGTH_SHORT).show()
                val bundle = Bundle().apply {
                    putBoolean("esAdmin",true)
                }
                findNavController().navigate(R.id.noticiasEquipoFragment)

            }
            .addOnFailureListener {e->//error al actualizar
                Log.d("noticiaactualizada","error:$e")
            }


        db.collection("noticias").document()

    }

    private fun listenersEditEtexts(editTexts:Array<EditText>) {
        for (et in editTexts) {//evento de edit text
            et.setOnFocusChangeListener { _, foco ->
                if (!foco) {
                    if (et.text.toString().isEmpty()) {
                        et.setError("Campo requerido")
                    }
                }
            }
        }
    }



    private fun agregarNoticia() {

        val db = FirebaseFirestore.getInstance()
        val nuevaNoticia = hashMapOf(
            "titulo" to binding.tituloAddNoticiaET.text.toString(),
            "descripcion" to binding.descripcionNoticiaET.text.toString(),
            "urlImg" to binding.urlNoticiaET.text.toString(),
            "fecha" to com.google.firebase.Timestamp.now()
        )
        db.collection("noticias").add(nuevaNoticia)
            .addOnSuccessListener { documentoReference->
                Toast.makeText(context, "Noticias agregada.", Toast.LENGTH_SHORT).show()
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder
                    .setMessage("Se ha agregado una nueva noticia\n" +
                            "Desea agregar otra?")
                    .setTitle("Registro agregado")
                    .setPositiveButton("Si") { dialog, which ->
                        limpiarEditTexts(arrayOf(binding.tituloAddNoticiaET,binding.descripcionNoticiaET,binding.urlNoticiaET))
                    }
                    .setNegativeButton("No") { dialog, which ->
                        // navega a las noticias
                        val bundle =Bundle().apply {
                            putBoolean("esAdmin",true)
                        }
                        findNavController().navigate(R.id.noticiasEquipoFragment)
                    }

                val dialog: AlertDialog = builder.create()
                dialog.show()//mestra el dialogo

            }.addOnFailureListener { error->
            Log.d("agregar",error.toString())//imprime error
            }

    }

    private fun limpiarEditTexts(editTexts: Array<EditText>) {
        editTexts.forEach { eT->eT.text.clear() }

    }

}