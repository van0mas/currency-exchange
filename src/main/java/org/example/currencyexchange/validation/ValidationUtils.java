package org.example.currencyexchange.validation;

public class ValidationUtils {

    public static boolean isValidCurrencyPath(String pathInfo) {
        return pathInfo != null && !pathInfo.equals("/");
    }

    public static boolean isValidCurrencyCode(String... codes) {
        if (codes == null || codes.length == 0) {
            return false;
        }
        for (String code : codes) {
            if (code == null || !code.matches("^[A-Z]{3}$")) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidCurrencyPairPath(String pathInfo) {
        if (pathInfo == null || pathInfo.length() != 7) return false;

        String base = pathInfo.substring(1, 4).toUpperCase();
        String target = pathInfo.substring(4).toUpperCase();
        return isValidCurrencyCode(base, target);
    }
}

