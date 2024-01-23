package start;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class StringParrten {
    public static void main(String[] args) {
        String input = "   This   is   an   example   string   with   extra   spaces.   ";

        // 去除多余的空格并格式化
        String formattedString = input.trim().replaceAll("\\s+", " ");

//        System.out.println("Original String: \"" + input + "\"");
//        System.out.println("Formatted String: \"" + formattedString + "\"");

    }

    private static String getVariableNameFromPattern(String input, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);

        if (m.find()) {
            return m.group(1); // 返回第一个捕获组的内容，即变量名 A
        }

        return null; // 没有找到匹配，返回null
    }
}
