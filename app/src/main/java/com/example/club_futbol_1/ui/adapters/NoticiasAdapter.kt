package com.example.club_futbol_1.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.example.club_futbol_1.R
import com.example.club_futbol_1.model.Noticia
import com.squareup.picasso.Picasso

class NoticiasAdapter(
    private val esAdministrador:Boolean,private
    val noticias: MutableList<Noticia>,
    private val  editarNoticia:(noticia:Noticia)->Unit,
    private val eliminarNoticia:(idNoticia:String)->Unit
) :
    RecyclerView.Adapter<NoticiasAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tituloNoticia: TextView
        val descripcionNoticia: TextView
        val imagenNoticia: ImageView
        val notivias_btn:Button
        val eliminarNoticiaBtn:Button

        init {
            // Define click listener for the ViewHolder's View
            tituloNoticia = view.findViewById(R.id.tituloNoticia)
            descripcionNoticia = view.findViewById(R.id.descripcionNoticia)
            imagenNoticia = view.findViewById(R.id.imagenNoticia)
            notivias_btn = view.findViewById(R.id.noticiasBtn)
            eliminarNoticiaBtn = view.findViewById(R.id.eliminarNoticiaBtn)
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

        if(esAdministrador){// si es admin habilto controles edision y eliminar
            viewHolder.notivias_btn.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.context,R.color.verde))
            viewHolder.notivias_btn.text="Editar"
            val noticia =Noticia(
                id = noticias[position].id!!,
                titulo = noticias[position].titulo!!,
                descripcion = noticias[position].descripcion!!,
                urlImagen = noticias[position].urlImagen!!

            )
            viewHolder.notivias_btn.setOnClickListener { editarNoticia(noticia) }
            viewHolder.eliminarNoticiaBtn.setOnClickListener { eliminarNoticia(noticias[position].id!!) }

            //eliminar
            viewHolder.eliminarNoticiaBtn.visibility=View.VISIBLE
        }
        viewHolder.tituloNoticia.text = noticias[position].titulo
        viewHolder.descripcionNoticia.text = noticias[position].descripcion
        Picasso.get()
            .load(noticias[position].urlImagen)
            .into(viewHolder.imagenNoticia)

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = noticias.size

}