package ar.edu.itba.hci.android.api

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.util.*

class DateTypeAdapter : TypeAdapter<Date>() {
    override fun write(out: JsonWriter?, value: Date?) {
        when(value) {
            null -> out?.nullValue()
            else -> out?.value(value.time)
        }
    }

    override fun read(input: JsonReader?): Date? {
        if(input == null) return null
        return Date(input.nextLong())
    }
}
