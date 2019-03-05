package com.burakekmen.weatherapp.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.burakekmen.weatherapp.ui.PlaceDetailActivity
import com.burakekmen.weatherapp.R
import com.burakekmen.weatherapp.model.NearPlaceModel
import com.burakekmen.weatherapp.viewholder.RcListViewHolder

class RcListAdapter(context: Context?, cityList:MutableList<NearPlaceModel>?): RecyclerView.Adapter<RcListViewHolder>() {

    private var context = context
    private var cityList = cityList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RcListViewHolder {
        var inflater = LayoutInflater.from(parent?.context).inflate(R.layout.rclist_item, parent, false)

        return RcListViewHolder(inflater)
    }



    override fun onBindViewHolder(holder: RcListViewHolder, position: Int) {

        var city = getItem(position)

        holder.cityName.text = city?.title
        holder.cityType.text = city?.location_type

        holder.cardView!!.setOnClickListener {
            detaySayfasinaGit(city)
        }

    }


    private fun detaySayfasinaGit(city: NearPlaceModel?) {

        var intent = Intent(context, PlaceDetailActivity::class.java)
        intent.putExtra("City", city)
        context?.startActivity(intent)
    }


    override fun getItemCount(): Int {
        return cityList!!.size
    }

    private fun getItem(position:Int):NearPlaceModel?{
        return cityList!![position]
    }

}