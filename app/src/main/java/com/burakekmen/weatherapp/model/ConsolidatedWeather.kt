package com.burakekmen.weatherapp.model

data class ConsolidatedWeather(
var weather_state_abbr:String,
var applicable_date:String,
var min_temp:Double,
var max_temp:Double
)