package com.example.club_futbol_1.ui.fragments_admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.club_futbol_1.R
import com.example.club_futbol_1.Utils
import com.example.club_futbol_1.databinding.FragmentAgregarEditarProductoBinding
import com.example.club_futbol_1.model.Producto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class AgregarEditarProductoFragment : Fragment() {

    private var _binding: FragmentAgregarEditarProductoBinding?=null
    private val binding get()= _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAgregarEditarProductoBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        var productoEditar:Producto?=null
        var editTexts= arrayOf(binding.aEProductoNombreET,binding.aEProductoDescripcionET,binding.aEProductoPrecioET,binding.aEProductoURLimgET)
        arguments?.let { it->
            productoEditar=it.getParcelable("productoEditar")
        }
        if (productoEditar==null){//agrega nu nuevo producto
            binding.agregarEditarProductoBtn.setOnClickListener {
                val validacionEditText=Utils.validarEditText(editTexts)
                if (validacionEditText==null){//todos los edit text llenos
                    agregarProducto()
                }
            }
        }else{//edita producto
            binding.aEProductoTitulo.text="Editar producto"
            binding.agregarEditarProductoBtn.text = "Editar"
            cargarDatosEnEditText(productoEditar)
            binding.agregarEditarProductoBtn.setOnClickListener {
                val validacionEditText=Utils.validarEditText(editTexts)
                if (validacionEditText==null){//todos los edit text llenos
                    editarProducto(productoEditar!!.id_document)
                }
            }
        }

        return binding.root
    }

    private fun cargarDatosEnEditText(productoEditar: Producto?) {//para editar
        productoEditar?.let {
            binding.aEProductoNombreET.setText(productoEditar.nombre_producto)
            binding.aEProductoDescripcionET.setText(productoEditar.descripcion_producto)
            binding.aEProductoPrecioET.setText(productoEditar.precio_producto.toString())
            binding.aEProductoURLimgET.setText(productoEditar.url_img_producto)

        }
    }

    private fun editarProducto(idDocument:String) {

        val db = FirebaseFirestore.getInstance()
        val producto = hashMapOf(
            "nombre_producto" to  binding.aEProductoNombreET.text.toString(),
            "descripcion_producto" to binding.aEProductoDescripcionET.text.toString(),
            "precio_producto" to binding.aEProductoPrecioET.text.toString().toDouble(),
            "url_img_producto" to  binding.aEProductoURLimgET.text.toString()
        )
        db.collection("tienda")
            .document(idDocument)
            .set(producto, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(context,"Producto actualizado.--",Toast.LENGTH_LONG).show()
                val bundle =Bundle().apply {
                    putBoolean("esAdmin",true)
                }
                findNavController().navigate(R.id.action_agregarEditarProductoFragment_to_tiendaFragment,bundle)
            }.addOnFailureListener { e->
                Log.e("actualizarProducto",e.toString())
            }
    }

    private fun agregarProducto() {

        val db = FirebaseFirestore.getInstance()
        val producto = hashMapOf(
            "nombre_producto" to  binding.aEProductoNombreET.text.toString(),
            "descripcion_producto" to binding.aEProductoDescripcionET.text.toString(),
            "precio_producto" to binding.aEProductoPrecioET.text.toString().toDouble(),
            "url_img_producto" to  binding.aEProductoURLimgET.text.toString()
        )

        db.collection("tienda")
            .add(producto)
            .addOnSuccessListener {
                val bundle =Bundle().apply {
                    putBoolean("esAdmin",true)
                }
                Toast.makeText(context,"Producto agregado",Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_agregarEditarProductoFragment_to_tiendaFragment,bundle)
            }
            .addOnFailureListener {e->
                Log.e("agregarproducto",e.toString())

            }

    }


}