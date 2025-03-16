package com.example.club_futbol_1.model

import android.os.Parcel
import android.os.Parcelable


data class Producto(
    val id_document:String,
    val precio_producto:Double,
    val url_img_producto:String,
    val nombre_producto:String,
    val descripcion_producto:String,

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id_document)
        parcel.writeDouble(precio_producto)
        parcel.writeString(url_img_producto)
        parcel.writeString(nombre_producto)
        parcel.writeString(descripcion_producto)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Producto> {
        override fun createFromParcel(parcel: Parcel): Producto {
            return Producto(parcel)
        }

        override fun newArray(size: Int): Array<Producto?> {
            return arrayOfNulls(size)
        }
    }
}
