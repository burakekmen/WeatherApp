package com.burakekmen.weatherapp.viewholder

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.burakekmen.weatherapp.R

class PlaceDetailRcListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

    var maxTemp = itemView.findViewById<TextView>(R.id.place_detail_rcList_item_maxTemp)
    var minTemp = itemView.findViewById<TextView>(R.id.place_detail_rcList_item_minTemp)
    var tempDate = itemView.findViewById<TextView>(R.id.place_detail_rcList_item_date)
    var weatherImage = itemView.findViewById<ImageView>(R.id.place_detail_rcList_item_weatherType)

}