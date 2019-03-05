package com.burakekmen.weatherapp.network

import com.burakekmen.weatherapp.model.CityDetail
import com.burakekmen.weatherapp.model.NearPlaceModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("location/search")
    fun getNearPlaces(@Query("lattlong") lati: String?): Call<List<NearPlaceModel>>

    @GET("location/{woeid}/")
    fun getCityTemps(@Path("woeid") woeid: Int?): Call<CityDetail>


}