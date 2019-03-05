package com.burakekmen.weatherapp.viewholder

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.burakekmen.weatherapp.R

class RcListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var cityName = itemView.findViewById<TextView>(R.id.rcList_item_cityName)
    var cityType = itemView.findViewById<TextView>(R.id.rcList_item_itemType)
    var cardView = itemView.findViewById<CardView>(R.id.rcList_item_cardView)

}