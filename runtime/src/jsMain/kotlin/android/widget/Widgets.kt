package android.widget

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Text

/** Compose-Web backed equivalents for a small, functional Android widget subset. */
@Composable
fun TextView(text: String, color: Int? = null) {
    org.jetbrains.compose.web.dom.P(attrs = { style { fontSize(18.px); color?.let { this.color(Color(android.graphics.Color.toCss(it))) } } }) { Text(text) }
}

@Composable
fun Button(text: String, onClick: () -> Unit) {
    Button(attrs = { onClick { onClick() }; style { padding(10.px, 16.px); borderRadius(8.px) } }) { Text(text) }
}
