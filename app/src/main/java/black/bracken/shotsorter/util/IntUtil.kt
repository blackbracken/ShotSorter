package black.bracken.shotsorter.util

/**
 * @author BlackBracken
 */
object IntUtil {

    fun parseIntOrDefault(parsed: String, defaultValue: Int): Int {
        try {
            return Integer.parseInt(parsed)
        } catch (ex: NumberFormatException) {
            return defaultValue
        }

    }

}
