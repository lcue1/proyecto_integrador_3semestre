package com.example.club_futbol_1.model

import android.os.Parcel
import android.os.Parcelable

data class Noticia(
    val id:String?,
    val titulo:String?,
    val descripcion:String?,
    val urlImagen:String?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(titulo)
        parcel.writeString(descripcion)
        parcel.writeString(urlImagen)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Noticia> {
        override fun createFromParcel(parcel: Parcel): Noticia {
            return Noticia(parcel)
        }

        override fun newArray(size: Int): Array<Noticia?> {
            return arrayOfNulls(size)
        }
    }
}
