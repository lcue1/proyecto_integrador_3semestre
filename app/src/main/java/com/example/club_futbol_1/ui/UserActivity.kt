package com.example.club_futbol_1.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import com.google.firebase.firestore.Query
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.club_futbol_1.R
import com.example.club_futbol_1.databinding.ActivityUserBinding
import com.example.club_futbol_1.model.Usuario
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentChange
import com.squareup.picasso.Picasso

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    private lateinit var drawerLayout: DrawerLayout
    private var usuario: Usuario?=null

    //notificacion atributos
    val CHANNEL_ID ="129"
    val NOTIFICATION_ID = 123


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
         usuario = intent.getParcelableExtra<Usuario>("usuario")
        usuario?.let {
         //   cargarFragmento(R.id.tiendaFragment)
            permisoYMostrarNotificacion()
            seleccionarFragmentoMenu()
            escucharNuevasNoticias()
            createNotificationChannel()//canal notificaciones
        }
    }



    private fun escucharNuevasNoticias() {
        val db = FirebaseFirestore.getInstance()
        var esPrimeraCarga = true // Bandera para ignorar el primer lote

        db.collection("noticias")
            .orderBy("fecha", Query.Direction.DESCENDING) // Ordena por fecha
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.e("notificacionesNoticias", "Error al escuchar noticias", e)
                    return@addSnapshotListener
                }

                if (snapshots != null) {

                    if (esPrimeraCarga) {//ignora la primera carga de datos
                        esPrimeraCarga = false
                        return@addSnapshotListener
                    }

                    for (change in snapshots.documentChanges) {
                        if (change.type == DocumentChange.Type.ADDED) { // Solo documentos nuevos
                            val doc = change.document
                            val titulo = doc.getString("titulo") ?: "Nueva Noticia"
                            val descripcion = doc.getString("descripcion") ?: "Revisa las novedades"
                            Log.d("notificacionesNoticias", "Nueva noticia agregada: $titulo")
                            permisoYMostrarNotificacion(crearNotificacion())//lanzar notificacion
                        }
                    }
                }
            }
    }

    private fun permisoYMostrarNotificacion(notificacionBuilder:NotificationCompat.Builder?=null) {
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@UserActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //solicitar permiso
                MaterialAlertDialogBuilder(this@UserActivity, com.google.android.material.R.style.MaterialAlertDialog_Material3)
                    .setTitle("Permiso de notificacion")
                    .setMessage("Te gustaria recibir notificaciones?")
                    .setPositiveButton("Si") { _, _ ->
                        val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.parse("package:$packageName")
                        startActivity(intent)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
                return@with
            }


            // notificationId is a unique int for each notification that you must define.
            if (notificacionBuilder!=null)
                notify(NOTIFICATION_ID, notificacionBuilder.build())
        }
    }
    private fun crearNotificacion(): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.degradado_main_activity)
            .setContentTitle("My notification")
            .setContentText("Much longer text that cannot fit one line...")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Much longer text that cannot fit one line..."))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun seleccionarFragmentoMenu() {

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        //establecer imagen de usuario

        val headerView = navigationView.getHeaderView(0) // Obtiene la vista del header
        val imageView = headerView.findViewById<ImageView>(R.id.imageViewHeader)
        Picasso.get()
            .load(usuario!!.urlImagen)
            .into(imageView)
        headerView.findViewById<TextView>(R.id.tvUsername).text=usuario!!.nombre



        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START) // Abre el menú
        }

        // Manejo de clics en el Navigation Drawer
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_noticias -> {
                    val bundle = Bundle().apply {
                        putParcelable("usuario", usuario)
                    }
                    cargarFragmento(R.id.noticiasEquipoFragment,bundle)
                }
                R.id.nav_perfil -> {
                    val bundle = Bundle().apply {
                        putParcelable("usuario", usuario)
                    }
                    cargarFragmento(R.id.infoUssuarioFragment,bundle)
                }
                R.id.nav_tienda -> {
                    val bundle = Bundle().apply {
                        putParcelable("usuario", usuario)
                    }
                    cargarFragmento(R.id.tiendaFragment, bundle)
                }
                R.id.menu_add_noticia -> {
                    val bundle = Bundle().apply {
                        putParcelable("usuario", usuario)
                    }
                    cargarFragmento(R.id.addNoticiaFragment,bundle)
                }
                R.id.menu_editar_noticia -> {
                    val bundle = Bundle().apply {
                        putParcelable("usuario", usuario)
                        putBoolean("esAdmin",true)
                    }
                    cargarFragmento(R.id.noticiasEquipoFragment,bundle)
                }
                R.id.administrarTienda -> {
                    val bundle = Bundle().apply {
                        putParcelable("usuario", usuario)
                        putBoolean("esAdmin",true)
                    }
                    cargarFragmento(R.id.tiendaFragment, bundle)
                }

            }
            drawerLayout.closeDrawer(GravityCompat.START) // Cierra el menú después de seleccionar
            true
        }
    }

    private fun cargarFragmento(fragmento_a_mostrar:Int,bundle:Bundle) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Para forzar la recarga, eliminamos el fragmento del back stack antes de navegar
        navController.popBackStack(fragmento_a_mostrar, true)

        // Opciones de navegación para evitar que se guarde en el back stack y forzar recarga
        val navOptions = NavOptions.Builder()
            .setPopUpTo(fragmento_a_mostrar, true) // Elimina el fragmento si ya existe
            .setLaunchSingleTop(false) // Permite recrear el fragmento
            .build()

        navController.navigate(fragmento_a_mostrar, bundle, navOptions)

    }

}