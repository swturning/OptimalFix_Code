package start.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularMatchUtil {

    public static String[] uint256Match(String text) {

        String pattern = "\\buint256\\s+(\\w+)\\s*=\\s*(\\d+)\\b";

        return  checkStringForPattern(text, pattern);

    }

    public static String[] intMatch(String text){
        String pattern = "\\bint\\s+(\\w+)\\s*=\\s*(\\d+)\\b";

        return  checkStringForPattern(text, pattern);

    }

    public static String[] boolMatch(String text){
        String pattern = "\\bbool\\s+(\\w+)\\s*=\\s*(true|false)\\b";

        return  checkStringForPattern(text, pattern);

    }

    private static  String[] checkStringForPattern(String input, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);

        if (m.find()){
            String variableName = m.group(1);
            String value = m.group(2);
            return new String[]{variableName, value};
        }

        return null;
    }
}
