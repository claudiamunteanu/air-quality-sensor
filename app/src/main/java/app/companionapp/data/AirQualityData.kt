package app.companionapp.data

data class AirQualityData(
    val timestamp: Long?= null,
    val pressure: Float? = null,
    val humidity: Float? = null,
    val temperature: Float? = null
)
