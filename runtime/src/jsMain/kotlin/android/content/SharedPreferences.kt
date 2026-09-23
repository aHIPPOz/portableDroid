package android.content

import kotlinx.browser.window

/** localStorage-backed implementation of Android SharedPreferences. */
class SharedPreferences internal constructor(private val prefix: String) {
    fun getInt(key: String, defaultValue: Int): Int = window.localStorage.getItem(key(key))?.toIntOrNull() ?: defaultValue
    fun getString(key: String, defaultValue: String?): String? = window.localStorage.getItem(key(key)) ?: defaultValue
    fun contains(key: String): Boolean = window.localStorage.getItem(key(key)) != null
    fun edit(): Editor = Editor(prefix)

    private fun key(key: String) = "$prefix.$key"

    class Editor internal constructor(private val prefix: String) {
        private val writes = mutableMapOf<String, String?>()
        fun putInt(key: String, value: Int): Editor = apply { writes[key] = value.toString() }
        fun putString(key: String, value: String?): Editor = apply { writes[key] = value }
        fun remove(key: String): Editor = apply { writes[key] = null }
        fun apply() { commit() }
        fun commit(): Boolean {
            writes.forEach { (key, value) ->
                val storageKey = "$prefix.$key"
                if (value == null) window.localStorage.removeItem(storageKey) else window.localStorage.setItem(storageKey, value)
            }
            return true
        }
    }
}
