package portable.runtime

import androidx.compose.runtime.Composable
import kotlinx.browser.document
import org.jetbrains.compose.web.renderComposable

/** Owns the browser document root and the current Android Activity content. */
object PortableRuntime {
    private var content: (@Composable () -> Unit)? = null

    fun setContent(next: @Composable () -> Unit) {
        check(content == null) { "Activity content may only be configured once during bootstrap." }
        content = next
    }

    fun launch(rootId: String = "root") {
        val root = requireNotNull(document.getElementById(rootId)) { "Missing PWA root element #$rootId" }
        val page = requireNotNull(content) { "Activity did not call setContent before launch." }
        renderComposable(root) { page() }
    }
}
