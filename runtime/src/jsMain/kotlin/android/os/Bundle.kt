package android.os

/** Minimal browser implementation of Android's key/value state container. */
class Bundle {
    private val values = mutableMapOf<String, Any?>()

    fun putString(key: String, value: String?) { values[key] = value }
    fun getString(key: String): String? = values[key] as? String
    fun containsKey(key: String): Boolean = key in values
}
