package com.example.club_futbol_1.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

 object Dialogos{
     fun mostrarDialogoConfirmacion(
         contest:Context,
         titulo:String, descripcion:String,
         accionAfirmativa:()->Unit,
         accionNegativa: (() -> Unit)? =null
     ) {
         val builder: AlertDialog.Builder = AlertDialog.Builder(contest)
         builder
             .setMessage(descripcion)
             .setTitle(titulo)
             .setPositiveButton("Si") { dialog, which ->
                 accionAfirmativa()
             }
             .setNegativeButton("No") { dialog, which ->
                 if (accionNegativa != null) {
                     accionNegativa()
                 }
             }

         val dialog: AlertDialog = builder.create()
         dialog.show()


     }
}


