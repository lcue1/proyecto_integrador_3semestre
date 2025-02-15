package com.example.club_futbol_1.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.club_futbol_1.R
import com.example.club_futbol_1.model.Noticia
import com.example.club_futbol_1.model.Producto
import com.squareup.picasso.Picasso


class TiendaAdapter(private val productos: MutableList<Producto>) :
    RecyclerView.Adapter<TiendaAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val precio: TextView
        val nombre_producto: TextView
        val descripcion_producto: TextView
        val imagen_producto: ImageView

        init {
            // Define click listener for the ViewHolder's View
            precio = view.findViewById(R.id.precio_producto)
            nombre_producto = view.findViewById(R.id.titulo_producto)
            descripcion_producto = view.findViewById(R.id.descripcion_producto)
            imagen_producto = view.findViewById(R.id.imagen_producto)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.tienda_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.precio.text = productos[position].precio_producto.toString()+"$"
        viewHolder.nombre_producto.text = productos[position].nombre_producto

        Picasso.get()
            .load(productos[position].url_img_producto)
            .into(viewHolder.imagen_producto)

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = productos.size

}