package android.graphics

/** ARGB color helpers compatible with common Android call sites. */
object Color {
    const val WHITE: Int = -0x1
    const val BLACK: Int = -0x1000000
    const val TRANSPARENT: Int = 0x00000000

    fun rgb(red: Int, green: Int, blue: Int): Int = argb(255, red, green, blue)
    fun argb(alpha: Int, red: Int, green: Int, blue: Int): Int =
        ((alpha and 0xff) shl 24) or ((red and 0xff) shl 16) or ((green and 0xff) shl 8) or (blue and 0xff)

    fun toCss(color: Int): String = "#" + listOf((color ushr 16) and 0xff, (color ushr 8) and 0xff, color and 0xff)
        .joinToString("") { it.toString(16).padStart(2, '0') }
}
