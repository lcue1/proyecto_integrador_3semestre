package com.example.club_futbol_1.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.example.club_futbol_1.databinding.FragmentInfoUssuarioBinding
import com.example.club_futbol_1.model.Usuario
import com.example.club_futbol_1.utils.Dialogos
import com.squareup.picasso.Picasso


class InfoUssuarioFragment : Fragment() {
    private var _binding: FragmentInfoUssuarioBinding? = null
    private val binding get() = _binding!! // Acceso seguro a binding
    private var usuario:Usuario?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {// obtiene usuario
            usuario = it.getParcelable("usuario")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoUssuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usuario?.let {
            cargarDatosUsuario()
            clickListener()
        }

    }

    private fun clickListener() {
        binding.salitBtn.setOnClickListener { Dialogos.mostrarDialogoConfirmacion(
            contest = requireContext(),
            titulo = "Salir",
            descripcion = "QUieres salir ?",
            accionAfirmativa = {
                requireActivity().finishAffinity()
            }
        ) }
    }

    private fun cargarDatosUsuario() {
        binding.infoUsuarioNombre.text = usuario!!.nombre
        Picasso.get()
            .load(usuario!!.urlImagen)
            .into(binding.infoUsuarioImagen)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evita fugas de memoria
    }


}