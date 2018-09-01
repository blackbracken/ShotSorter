package black.bracken.shotsorter.util

import android.text.Editable
import android.text.TextWatcher

/**
 * @author BlackBracken
 */
class SimpleTextWatcher(
        private val action: (CharSequence, Int, Int, Int) -> Unit
) : TextWatcher {

    override fun onTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) = action(charSequence, start, count, after)

    override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(editable: Editable) {}

}
