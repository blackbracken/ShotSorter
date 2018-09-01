package black.bracken.shotsorter.util.extension

import android.content.SharedPreferences

/**
 * @author BlackBracken
 */

fun SharedPreferences.write(writing: (SharedPreferences.Editor).() -> Unit) {
    val editor = this.edit()
    try {
        writing(editor)
    } finally {
        editor.apply()
    }
}