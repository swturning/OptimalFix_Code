package start;

public class zheng {
    public static void main(String[] args) {
        String input = "k[v] -= a";

        // 使用正则表达式分割字符串
        String[] tokens = input.split("[+=*-/]");

        // 遍历分割后的字符串数组
        for (String token : tokens) {
            System.out.println(token);
        }
    }

    public static String addSpacingToOperators(String input) {
        String[] operators = {"+", "-", "*", "/", "+=", "-+"};
        for (String operator : operators) {
            input = input.replace(operator, " " + operator + " ");
        }
        return input;
    }
}