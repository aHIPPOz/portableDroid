package portable.runtime

/**
 * Linux target boundary. DBus capability adapters and the Compose Desktop host belong here.
 * The browser facade deliberately does not pretend that JNI or Android Binder are native Linux APIs.
 */
object LinuxRuntime {
    val architecture: String = "x86_64"
}
