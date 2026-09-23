package android.content

import portable.runtime.PortableRuntime

/** Browser implementation of Android's process context surface. */
open class Context {
    fun getSharedPreferences(name: String, mode: Int = MODE_PRIVATE): SharedPreferences {
        require(mode == MODE_PRIVATE) { "Only MODE_PRIVATE is meaningful in a browser context." }
        return SharedPreferences("portabledroid.$name")
    }

    open fun startActivity(intent: Intent) {
        PortableRuntime.startActivity(intent)
    }

    companion object {
        const val MODE_PRIVATE: Int = 0
    }
}
