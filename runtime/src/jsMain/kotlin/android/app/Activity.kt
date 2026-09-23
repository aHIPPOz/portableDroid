package android.app

import android.content.Context
import android.os.Bundle
import androidx.compose.runtime.Composable
import portable.runtime.PortableRuntime

/** Browser activity lifecycle facade. */
open class Activity : Context() {
    open fun onCreate(savedInstanceState: Bundle?) = Unit

    protected fun setContent(content: @Composable () -> Unit) {
        PortableRuntime.setContent(content)
    }
}
