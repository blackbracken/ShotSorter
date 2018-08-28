package black.bracken.shotsorter.util

import android.text.Editable
import android.text.TextWatcher

/**
 * @author BlackBracken
 */
interface SimpleTextWatcher : TextWatcher {

    override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(editable: Editable) {}

}
