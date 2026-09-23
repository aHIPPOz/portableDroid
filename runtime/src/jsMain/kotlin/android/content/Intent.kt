package android.content

import android.net.Uri
import android.os.Bundle

/** Portable subset of Android intent routing and typed extras. */
class Intent(var action: String? = null, var data: Uri? = null) {
    private val extras = Bundle()

    fun setAction(value: String): Intent = apply { action = value }
    fun setData(value: Uri?): Intent = apply { data = value }
    fun putExtra(key: String, value: String?): Intent = apply { extras.putString(key, value) }
    fun getStringExtra(key: String): String? = extras.getString(key)

    companion object {
        const val ACTION_VIEW: String = "android.intent.action.VIEW"
    }
}
