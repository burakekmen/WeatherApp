package com.burakekmen.weatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.burakekmen.weatherapp.R
import com.burakekmen.weatherapp.Utils
import com.burakekmen.weatherapp.adapter.RcListAdapter
import com.burakekmen.weatherapp.model.NearPlaceModel
import com.burakekmen.weatherapp.network.ApiClient
import com.burakekmen.weatherapp.network.ApiInterface
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private var utils:Utils ?=null
    private var doubleBackToExitPressedOnce = false
    private var lati = 0.0
    private var longlati = 0.0
    private var apiInterface:ApiInterface?=null
    private var nearPlaces = mutableListOf<NearPlaceModel>()

    val MY_PERMISSIONS_REQUEST_LOCATION = 100
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        acilisHazirlik()
        if(isOnlineCheck()) {
                utils?.dialogGoster()
                getNearPlaces()
        }
        else {
            uyariGoster("İnternet Bağlantınızı Kontrol Ediniz!")
        }
    }


    fun acilisHazirlik(){
        utils = Utils(this)
        apiInterface = ApiClient.client?.create(ApiInterface::class.java)
        activity_main_swipeLayout?.setOnRefreshListener(this)


    }


    fun uyariGoster(message:String){
        val snackbar = Snackbar.make(activity_main_layout, message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    fun isOnlineCheck():Boolean{
        return utils!!.isOnline()
    }


    override fun onResume(){
        super.onResume()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if(checkLocationPermission()){
            getLocation()
        }

    }

    override fun onRefresh() {
        if (isOnlineCheck()) {
            getLocation()
            utils?.dialogGoster()
            getNearPlaces()

        } else {
            uyariGoster("İnternet Bağlantınızı Kontrol Ediniz!")
        }
    }



    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                AlertDialog.Builder(this)
                    .setTitle("Lokasyon İzni")
                    .setMessage("Lokasyon hizmetini kullanmanız için izin vermeniz gerekektedir!")
                    .setPositiveButton("Tamam", DialogInterface.OnClickListener { dialogInterface, i ->

                        ActivityCompat.requestPermissions(this@MainActivity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION)
                    })
                    .create()
                    .show()

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION)
            }
            return false
        } else {
            return true
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        getLocation()
                    }
                } else {
                    // permission denied!
                }
                return
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location!=null){
                    lati = location.latitude
                    longlati = location.longitude
                } else {
                    Log.e("TAG","location is null")
                }
            }

    }


    fun getNearPlaces(){

        var lattlongi = lati.toString() + "," + longlati.toString()

        apiInterface?.getNearPlaces(lattlongi)?.enqueue(object : Callback<List<NearPlaceModel>> {

            override fun onResponse(call: Call<List<NearPlaceModel>>?, response: Response<List<NearPlaceModel>>?) {

                if (response!!.isSuccessful) {

                    nearPlaces.clear()
                    nearPlaces.addAll(response.body()!!)

                    if(nearPlaces.size > 0){
                        var placeAdapter = RcListAdapter(applicationContext, nearPlaces)
                        listeyeGonder(placeAdapter)
                        utils?.dialogGosterme()
                        if (activity_main_swipeLayout?.isRefreshing!!)
                            activity_main_swipeLayout?.isRefreshing = false
                    }

                    utils?.dialogGosterme()
                } else {
                    utils?.dialogGosterme()
                }
            }

            override fun onFailure(call: Call<List<NearPlaceModel>>?, t: Throwable?) {

                Log.e("BASARISIZ", t?.message)
                utils?.dialogGosterme()
                if (activity_main_swipeLayout?.isRefreshing!!)
                    activity_main_swipeLayout?.isRefreshing = false
                uyariGoster("Veriler yüklenirken hata oluştu!")
            }

        })


    }



    fun listeyeGonder(adapter:RcListAdapter){
        activity_main_nearPlaceRL.adapter = adapter

        var myLayoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        activity_main_nearPlaceRL.layoutManager = myLayoutManager
    }


    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true

            Snackbar.make(
                activity_main_layout, // Parent view
                "Çıkış için tekrar Geri tuşuna basınız!", // Message to show
                Snackbar.LENGTH_SHORT // How long to display the message.
            ).show()

            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else {
            super.onBackPressed()
            return
        }
    }
}
