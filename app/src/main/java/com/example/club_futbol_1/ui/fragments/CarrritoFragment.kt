package com.example.club_futbol_1.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.club_futbol_1.R
import com.example.club_futbol_1.databinding.FragmentCarrritoBinding
import com.example.club_futbol_1.model.Producto
import com.example.club_futbol_1.ui.adapters.CarritoAdapter
import java.util.ArrayList


class CarrritoFragment : Fragment() {

    private var _binding: FragmentCarrritoBinding?=null
    private val binding get()= _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val productosSeleccionados = arguments?.getParcelableArrayList<Producto>("productosCarrito")

        _binding = FragmentCarrritoBinding.inflate(inflater, container, false)

        binding.btnRegresar.setOnClickListener {
            findNavController().navigate(R.id.action_carrritoFragment_to_tiendaFragment,)
        }
        productosSeleccionados?.let {

            cargarProductosEnUI(productosSeleccionados)
        }

        return binding.root

    }
    private fun cargarProductosEnUI(productosSeleccionados: ArrayList<Producto>) {

        // Calcular los valores iniciales (cantidad y precio total)
        val cantidadProductos = productosSeleccionados.size // numero productos seleccionados
        val precioTotal = productosSeleccionados.sumOf { it.precio_producto } // Total de los precios de todos los productos
        establecerCantidades(cantidadProductos,precioTotal)// valores iniciales

        val adaptador = CarritoAdapter(//carga adapter
            producto_carrito = productosSeleccionados,
            obtenerCantidadProductos = { cantidadProductos, precioTotal ->
                establecerCantidades(cantidadProductos,precioTotal)//actualiza valores
            }
        )

        val recyclerView: RecyclerView = binding.carritoRecycle
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adaptador

    }

    private fun establecerCantidades(cantidadTotalProductos:Int, precioTotal:Double){
        binding.cantidaProductos.text = "Cantidad de productos: "+cantidadTotalProductos.toString()
        binding.precioTotalProductos.text =String.format("Total a pagar: %.2f",precioTotal)
    }


}