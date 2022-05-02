package app.companionapp.utils

import app.companionapp.data.AirQualityData
import com.google.gson.Gson

object SerializerUtil {
    private val gson = Gson()

    fun transformJsonToAirQualityData(json: String): AirQualityData {
        return gson.fromJson(json, AirQualityData::class.java)
    }
}