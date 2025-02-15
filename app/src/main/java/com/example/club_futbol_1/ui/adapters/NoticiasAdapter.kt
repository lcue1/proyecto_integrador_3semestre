package com.example.club_futbol_1.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.club_futbol_1.R
import com.example.club_futbol_1.model.Noticia
import com.squareup.picasso.Picasso

class NoticiasAdapter(private val noticias: MutableList<Noticia>) :
    RecyclerView.Adapter<NoticiasAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tituloNoticia: TextView
        val descripcionNoticia: TextView
        val imagenNoticia: ImageView

        init {
            // Define click listener for the ViewHolder's View
            tituloNoticia = view.findViewById(R.id.tituloNoticia)
            descripcionNoticia = view.findViewById(R.id.descripcionNoticia)
            imagenNoticia = view.findViewById(R.id.imagenNoticia)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.noticia_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.tituloNoticia.text = noticias[position].titulo
        viewHolder.descripcionNoticia.text = noticias[position].descripcion
        Picasso.get()
            .load(noticias[position].urlImagen)
            .into(viewHolder.imagenNoticia)

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = noticias.size

}