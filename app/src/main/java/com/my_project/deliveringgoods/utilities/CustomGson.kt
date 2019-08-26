package com.my_project.deliveringgoods.utilities

import com.google.gson.*
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.OffsetTime
import java.lang.reflect.Type
import java.util.*

internal class UUIDTypeAdapter : JsonSerializer<UUID?>, JsonDeserializer<UUID?> {

    override fun serialize(src: UUID?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return if (src == null) JsonNull.INSTANCE else JsonPrimitive(src.toString())
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): UUID? {
        if (json == null || json.isJsonNull) return null

        return UUID.fromString(json.asString)
    }
}


internal class OffsetDateTimeAdapter : JsonSerializer<OffsetDateTime?>, JsonDeserializer<OffsetDateTime?> {

    override fun serialize(src: OffsetDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return if (src == null) JsonNull.INSTANCE else JsonPrimitive(src.toString())
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): OffsetDateTime? {
        if (json == null || json.isJsonNull) return null

        return OffsetDateTime.parse(json.asString)
    }
}

internal class OffsetTimeAdapter : JsonSerializer<OffsetTime?>, JsonDeserializer<OffsetTime?> {

    override fun serialize(src: OffsetTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return if (src == null) JsonNull.INSTANCE else JsonPrimitive(src.toString())
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): OffsetTime? {
        if (json == null || json.isJsonNull) return null

        return OffsetTime.parse(json.asString)
    }
}


val gson: Gson by lazy { buildGson() }


private fun buildGson(): Gson {
    return GsonBuilder()
        .serializeNulls()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .registerTypeAdapter(UUID::class.java, UUIDTypeAdapter())
        .registerTypeAdapter(OffsetDateTime::class.java, OffsetDateTimeAdapter())
        .registerTypeAdapter(OffsetTime::class.java, OffsetTimeAdapter())
        .create()
}