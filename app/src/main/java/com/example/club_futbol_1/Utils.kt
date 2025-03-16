package com.example.club_futbol_1

import android.widget.EditText

object  Utils{

     fun validarEditText(editTexts: Array<EditText>): EditText? {//retorna null si todos los campos estan llenos
        return editTexts.find { et->
            val editTextVacio=et.text.toString().isEmpty()
            if (editTextVacio){
                et.setError("Campo obligatorio")///indica al usuario que un campo esta vacio
            }
            editTextVacio
        }//retorna null  o un edittext
    }

}