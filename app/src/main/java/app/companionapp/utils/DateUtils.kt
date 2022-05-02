package app.companionapp.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun convertDateToShortReadableDate(date: Date?): String {
        return if (date != null) {
            val formatter = SimpleDateFormat("MM/dd HH:mm:ss", Locale.getDefault())
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            formatter.format(date)
        } else {
            ""
        }
    }

}