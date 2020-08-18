package cc.foxa.kie

import com.google.gson.annotations.JsonAdapter
import java.util.*

data class Comment(
    val id: String,
    val content: String,
    val from: String,
    @JsonAdapter(TimestampToDateJsonDeserializer::class) val createdAt: Date
)