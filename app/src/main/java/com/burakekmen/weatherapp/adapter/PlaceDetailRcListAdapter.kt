package com.burakekmen.weatherapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.burakekmen.weatherapp.R
import com.burakekmen.weatherapp.model.ConsolidatedWeather
import com.burakekmen.weatherapp.viewholder.PlaceDetailRcListViewHolder
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class PlaceDetailRcListAdapter (context: Context?, tempList:MutableList<ConsolidatedWeather>?): RecyclerView.Adapter<PlaceDetailRcListViewHolder>() {

    private var context = context
    private var tempList = tempList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceDetailRcListViewHolder {
        var inflater = LayoutInflater.from(parent?.context).inflate(R.layout.place_detail_rclist_item, parent, false)

        return PlaceDetailRcListViewHolder(inflater)
    }



    override fun onBindViewHolder(holder: PlaceDetailRcListViewHolder, position: Int) {

        var temp = getItem(position)

        Picasso.get().load(context!!.getString(R.string.image_url) + temp?.weather_state_abbr + ".png").into(holder.weatherImage)
        holder.maxTemp.text = holder.maxTemp.text.toString() + temp?.max_temp?.toInt().toString() + "\u2103"
        holder.minTemp.text = holder.minTemp.text.toString() + temp?.min_temp?.toInt().toString() + "\u2103"


        holder.tempDate.text = temp?.applicable_date
    }



    override fun getItemCount(): Int {
        return tempList!!.size
    }

    private fun getItem(position:Int): ConsolidatedWeather?{
        return tempList!![position]
    }

}