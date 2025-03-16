package com.example.club_futbol_1.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.club_futbol_1.R
import com.example.club_futbol_1.model.Producto
import com.squareup.picasso.Picasso

class CarritoAdapter(private val producto_carrito: MutableList<Producto>, val obtenerCantidadProductos:(capntidadProductos:Int,precioTotal:Double)->Unit) :
    RecyclerView.Adapter<CarritoAdapter.ViewHolder>() {
    private val cantidadesSeleccionadas = mutableMapOf<Int, Int>() // Mapa para almacenar cantidades por posición
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagen_producto_carrito: ImageView
        val titulo_producto_carrito:TextView
        val precio_producto_carrito:TextView
        val cantidad_producto_carrito:Spinner

        init {
            // Define click listener for the ViewHolder's View
            imagen_producto_carrito = view.findViewById(R.id.imagen_producto_carrito)
            titulo_producto_carrito = view.findViewById(R.id.titulo_producto_carrito)
            precio_producto_carrito = view.findViewById(R.id.precio_producto_carrito)
            cantidad_producto_carrito = view.findViewById(R.id.cantidad_producto_carrito)
        }
    }

    private var sumaCantidadProducto=0
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_carrito, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Picasso.get()
            .load(producto_carrito[position].url_img_producto)
            .into(viewHolder.imagen_producto_carrito)
        viewHolder.titulo_producto_carrito.text = producto_carrito[position].nombre_producto
        viewHolder.precio_producto_carrito.text = producto_carrito[position].precio_producto.toString()+"$"

        //spiner
        val numeroProductosCompra= listOf("1","2","3")
        val adapter = ArrayAdapter(viewHolder.itemView.context, R.layout.spiner_item_cantidad, numeroProductosCompra)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom)

        // Asignar el adaptador al Spinner
        viewHolder.cantidad_producto_carrito.adapter = adapter

        //suma precios

        //error evenyo
        viewHolder.cantidad_producto_carrito.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val cantidadSeleccionada = numeroProductosCompra[pos].toInt()
                val adapterPosition = viewHolder.adapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    cantidadesSeleccionadas[adapterPosition] = cantidadSeleccionada

                     //Calcular el total de las cantidades y el total de precios
                    val sumaCantidadProductos = sumarCantidadProductos()  // Suma de todas las cantidades
                    val totalPrecio = calcularTotalPrecio()  // Total precio (cantidad * precio)

                    // Llamar a la función con ambos valores
                    obtenerCantidadProductos(sumaCantidadProductos, totalPrecio)

                    // Imprimir ambos valores para verificación


                }
            }


            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }



    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = producto_carrito.size

    private fun sumarCantidadProductos(): Int {
        return cantidadesSeleccionadas.values.sum()
    }
    private fun calcularTotalPrecio(): Double {
        var total = 0.0
        // Sumar el precio total por producto (cantidad * precio)
        for ((index, cantidad) in cantidadesSeleccionadas) {
            val precioProducto = producto_carrito.getOrNull(index)?.precio_producto ?: 0.0
            total += precioProducto * cantidad
        }
        return total
    }
}