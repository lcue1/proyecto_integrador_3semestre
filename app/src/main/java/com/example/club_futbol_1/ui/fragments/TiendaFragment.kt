package com.example.club_futbol_1.ui.fragments

import android.app.AlertDialog
import android.media.audiofx.DynamicsProcessing.BandBase
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
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
    private val productosSeleccionados= mutableListOf<Producto>()
    private var nItemsCarrito=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var esAdmin=false
        arguments?.let {
            esAdmin=it.getBoolean("esAdmin")
        }

        _binding = FragmentTiendaBinding.inflate(inflater, container, false)

        if (esAdmin){
            cargarProductos(true)
            binding.agregarProductoBtn.visibility=View.VISIBLE
            binding.agregarProductoBtn.setOnClickListener {
                findNavController().navigate(R.id.action_tiendaFragment_to_agregarEditarProductoFragment,)
            }
        }else{
            binding.btnItemsCarrito.setOnClickListener { abrirFragmentCarrito() }//boton citems agregados al carrito
            cargarProductos(false)

        }

        return binding.root
    }

    private fun abrirFragmentCarrito() {
        val bundle = Bundle().apply {
            putParcelableArrayList("productosCarrito", ArrayList(productosSeleccionados))
        }

        findNavController().navigate(R.id.action_tiendaFragment_to_carrritoFragment, bundle)
    }


    private fun cargarProductos(esAdmin:Boolean) {
        val db = FirebaseFirestore.getInstance()

        db.collection("tienda")
            .get()
            .addOnSuccessListener { result ->
                val productos= mutableListOf<Producto>()//productos para el adapter
                for (document in result) {
                    // Accede a los datos del documento
                    val producto=Producto(//creo un producto
                        id_document=document.id,
                        precio_producto = document.getDouble("precio_producto")?:0.0,
                        url_img_producto = document.getString("url_img_producto")?:"",
                        nombre_producto = document.getString("nombre_producto")?:"",
                        descripcion_producto = document.getString("descripcion_producto")?:""
                    )
                    productos.add(producto)//agrego a productos para enviar al adapter
                    //add coleccion productos
                }

                val customAdapter = TiendaAdapter(//creo adapter
                    esAdmin = esAdmin,
                    productos = productos,//envio productos para que se muestren en el tienda item
                    agregarAlCarrito = {// agrega o elimina la lista del carrito
                        productoSeleccionado->  agregarEliminarCarrito(productoSeleccionado)   },
                    editarProducto = {producto->
                        val bandle =Bundle().apply {
                            putParcelable("productoEditar",producto)
                        }
                        findNavController().navigate(R.id.action_tiendaFragment_to_agregarEditarProductoFragment,bandle)

                    },
                    eliminarProducto = {idProducto->
                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("Eliminar producto?\n" +
                                "id: $idProducto")
                            .setTitle("Eliminar")
                            .setPositiveButton("Si"){dialog,with->
                                eliminarProducto(idProducto)

                            }
                            .setNegativeButton("No",null)

                            val dialog = builder.create()
                            dialog.show()

                    }
                )
                //vinculacion del adapter con el recyclerview
                val recyclerView: RecyclerView = binding.noticiasRecycle
                recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
                recyclerView.adapter = customAdapter

            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error al obtener documentos.", exception)
            }
    }

    private fun eliminarProducto(idProducto: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("tienda")
            .document(idProducto)
            .delete()
            .addOnSuccessListener {
                val bundle = Bundle().apply {
                    putBoolean("esAdmin", true)
                }
                findNavController().navigate(R.id.tiendaFragment, bundle)
                Toast.makeText(context,"Producto eliminado.",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                Log.e("eliminiarProducto",e.toString())
            }
    }

    private fun agregarEliminarCarrito(productoSeleccionado:Producto){
        val  existeItemCarrito = productosSeleccionados.find { it->it.id_document==productoSeleccionado.id_document }
        if(existeItemCarrito==null){//agrega una sola vez
            productosSeleccionados.add(productoSeleccionado)//agrega a la mutablelist
            nItemsCarrito++//incrementa contador de tiems o productos
            binding.btnItemsCarrito.text="VVer "+nItemsCarrito.toString()//modifica boton items carrito
        }else{//elimina carrito
            productosSeleccionados.remove(productoSeleccionado)
            nItemsCarrito--
            binding.btnItemsCarrito.text="VVer "+nItemsCarrito.toString()

        }
        onOFBtnCarrito()//muestra o oculta botn carrito

        Log.d("productosCar",productosSeleccionados.toString())
    }


    private fun onOFBtnCarrito(){
        if (productosSeleccionados.isEmpty()) {//oculta boton carrito cuando la lista esta vacia
            binding.btnItemsCarrito.visibility = View.GONE
            return
        }
            binding.btnItemsCarrito.visibility = View.VISIBLE

    }
}