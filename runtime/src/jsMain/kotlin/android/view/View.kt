package android.view

/** Base portable widget type. Concrete widgets render through Compose Web. */
open class View {
    var id: Int = NO_ID
    companion object { const val NO_ID: Int = -1 }
}
