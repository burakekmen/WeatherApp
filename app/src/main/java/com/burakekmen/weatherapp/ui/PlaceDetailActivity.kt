package com.burakekmen.weatherapp.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.burakekmen.weatherapp.R
import com.burakekmen.weatherapp.Utils
import com.burakekmen.weatherapp.adapter.PlaceDetailRcListAdapter
import com.burakekmen.weatherapp.adapter.RcListAdapter
import com.burakekmen.weatherapp.model.CityDetail
import com.burakekmen.weatherapp.model.ConsolidatedWeather
import com.burakekmen.weatherapp.model.NearPlaceModel
import com.burakekmen.weatherapp.network.ApiClient
import com.burakekmen.weatherapp.network.ApiInterface
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_place_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.firebase.analytics.FirebaseAnalytics



class PlaceDetailActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {


    private var utils: Utils?=null
    private var apiInterface:ApiInterface?=null
    private var city:NearPlaceModel?=null
    private var tempList = mutableListOf<ConsolidatedWeather>()

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        acilisHazirlik()

        if(isOnlineCheck()) {

            city = intent.getParcelableExtra("City")

            eventKaydet()

            if(city != null) {
                utils?.dialogGoster()
                getCityDetail()
            }else
                uyariGoster("Şehir iletilirken sorun oluştu! Tekrar deneyiniz!")
        }
        else {
            uyariGoster("İnternet Bağlantınızı Kontrol Ediniz!")
        }
    }


    fun acilisHazirlik(){
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        utils = Utils(this)
        apiInterface = ApiClient.client?.create(ApiInterface::class.java)
        activity_place_detail_swipeLayout?.setOnRefreshListener(this)
    }


    fun eventKaydet(){
        mFirebaseAnalytics!!.logEvent(city!!.title, null)
    }

    fun uyariGoster(message:String){
        val snackbar = Snackbar.make(activity_main_layout, message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    fun isOnlineCheck():Boolean{
        return utils!!.isOnline()
    }

    override fun onRefresh() {
        if(isOnlineCheck()) {
            utils?.dialogGoster()
            getCityDetail()
        }else{
            uyariGoster("İnternet Bağlantınızı Kontrol Ediniz!")
        }
    }



    fun getCityDetail(){

        apiInterface?.getCityTemps(city?.woeid)?.enqueue(object : Callback<CityDetail> {

            override fun onResponse(call: Call<CityDetail>?, response: Response<CityDetail>?) {

                if (response!!.isSuccessful) {

                    tempList.clear()
                    tempList.addAll(response.body()!!.consolidated_weather!!.toMutableList())

                    if(tempList!!.size > 0){

                        activity_place_detail_cityName.text = city?.title
                        var cityDetailAdapter = PlaceDetailRcListAdapter(applicationContext, tempList)
                        listeyeGonder(cityDetailAdapter)
                        utils?.dialogGosterme()
                        activity_place_detail_swipeLayout?.isRefreshing = false
                    }

                    utils?.dialogGosterme()
                    activity_place_detail_swipeLayout?.isRefreshing = false

                } else {
                    utils?.dialogGosterme()
                    activity_place_detail_swipeLayout?.isRefreshing = false


                }
            }

            override fun onFailure(call: Call<CityDetail>?, t: Throwable?) {

                Log.e("BASARISIZ", t?.message)
                utils?.dialogGosterme()
                activity_place_detail_swipeLayout?.isRefreshing = false
                uyariGoster("Veriler yüklenirken hata oluştu!")
            }

        })


    }



    fun listeyeGonder(adapter: PlaceDetailRcListAdapter){
        activity_place_detail_rcList.adapter = adapter

        var myLayoutManager = LinearLayoutManager(this@PlaceDetailActivity, LinearLayoutManager.VERTICAL, false)
        activity_place_detail_rcList.layoutManager = myLayoutManager
    }




}
