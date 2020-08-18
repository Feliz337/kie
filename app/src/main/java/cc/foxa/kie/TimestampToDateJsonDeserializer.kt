package cc.foxa.kie

import com.google.gson.*
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class TimestampToDateJsonDeserializer : JsonDeserializer<Date> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Date {
        if (json != null) {
            if (json.isJsonPrimitive) {
                val timestamp = json.asString
                val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(timestamp)
                date.hours += 8
                return date
            }
        }
        return Date(0)
    }


}