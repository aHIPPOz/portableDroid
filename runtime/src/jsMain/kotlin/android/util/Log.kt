package android.util

/** Android-compatible logging facade backed by the browser developer console. */
@JsName("console")
external object LogConsole {
    fun log(message: String)
    fun info(message: String)
    fun warn(message: String)
    fun error(message: String)
}

object Log {
    fun d(tag: String, message: String): Int { LogConsole.log("D/$tag: $message"); return 0 }
    fun i(tag: String, message: String): Int { LogConsole.info("I/$tag: $message"); return 0 }
    fun w(tag: String, message: String): Int { LogConsole.warn("W/$tag: $message"); return 0 }
    fun e(tag: String, message: String): Int { LogConsole.error("E/$tag: $message"); return 0 }
}
