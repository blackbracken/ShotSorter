package black.bracken.shotsorter.util;

/**
 * @author BlackBracken
 */
public final class IntUtil {

    private IntUtil() {
    }

    public static int parseIntOrDefault(String parsed, int defaultValue) {
        try {
            return Integer.parseInt(parsed);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

}
