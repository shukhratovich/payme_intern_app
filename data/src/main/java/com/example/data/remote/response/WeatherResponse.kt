package com.example.data.remote.response

import com.example.domain.model.ui.WeatherUIData
import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("coord")
    val coordinates: Coordinates? = null,
    @SerializedName("weather")
    val weather: List<WeatherData>? = null,
    @SerializedName("base")
    val base: String? = null,
    @SerializedName("main")
    val main: Main? = null,
    @SerializedName("visibility")
    val visibility: Int? = null,
    @SerializedName("wind")
    val wind: Wind? = null,
    @SerializedName("rain")
    val rain: Rain? = null,
    @SerializedName("snow")
    val snow: Snow? = null,
    @SerializedName("clouds")
    val clouds: Clouds? = null,
    @SerializedName("dt")
    val dt: Long? = null,
    @SerializedName("sys")
    val sys: Sys? = null,
    @SerializedName("timezone")
    val timezone: Int? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("cod")
    val cod: Int? = null
)


data class Coordinates(
    @SerializedName("lon")
    val lon: Double? = null,
    @SerializedName("lat")
    val lat: Double? = null
)


data class Main(
    @SerializedName("temp")
    val temp: Double? = null,
    @SerializedName("feels_like")
    val feelsLike: Double? = null,
    @SerializedName("pressure")
    val pressure: Int? = null,
    @SerializedName("humidity")
    val humidity: Int? = null,
    @SerializedName("temp_min")
    val tempMin: Double? = null,
    @SerializedName("temp_max")
    val tempMax: Double? = null,
    @SerializedName("sea_level")
    val seaLevel: Int? = null,
    @SerializedName("grnd_level")
    val groundLevel: Int? = null
)

data class WeatherData(
    val id: Int? = null,
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null
)

data class Clouds(
    val all: Int? = null
)

data class Wind(
    val speed: Double? = null,
    val deg: Double? = null,
    val gust: Double? = null
)

data class Rain(
    @SerializedName("1h")
    val oneHour: Double? = null
)

data class Snow(
    @SerializedName("1h")
    val oneHour: Double? = null
)

data class Sys(
    val type: Int? = null,
    val id: Int? = null,
    val message: String? = null,
    val country: String? = null,
    val sunrise: Long? = null,
    val sunset: Long? = null
)


fun WeatherResponse.toData(): WeatherUIData {
    val temp = main?.temp?.minus(273.15)?.toInt()
    return WeatherUIData(
        temp = temp ?: 0,
        cityName = name ?: "",
        icon = weather?.get(0)?.icon ?: "",
        wind = wind?.speed.toString(),
        humidity = (main?.humidity ?: 0).toString(),
        description = weather?.get(0)?.description ?: "",
        mainDescription = weather?.get(0)?.main ?: ""
    )
}