package com.burakekmen.weatherapp.model

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.IntegerRes

data class NearPlaceModel(var title:String, var location_type:String, var woeid:Int, var lat_long:Float) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readFloat()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(location_type)
        parcel.writeInt(woeid)
        parcel.writeFloat(lat_long)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NearPlaceModel> {
        override fun createFromParcel(parcel: Parcel): NearPlaceModel {
            return NearPlaceModel(parcel)
        }

        override fun newArray(size: Int): Array<NearPlaceModel?> {
            return arrayOfNulls(size)
        }
    }
}
