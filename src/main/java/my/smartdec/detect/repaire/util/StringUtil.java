package my.smartdec.detect.repaire.util;

import org.w3c.dom.Node;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    //通过 >= <= < > 来分个算术表达式 分割为3部分 只返回第一部分
    //改为 有相关运算符号的部分
    public static String spiltExpressionReturnFirst(String expression){

        // 正则表达式用于匹配 >=、<=、> 或 <
        String regex = "(\\s*)(>=|<=|>|<|)(\\s*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);

        if (matcher.find()) {
            // 使用正则表达式的捕获组获取分割后的三部分
            String firstPart = expression.substring(0, matcher.start()).trim();
//            String operator = matcher.group(2).trim();
//            String secondPart = expression.substring(matcher.end()).trim();

            return firstPart;
        }

        return null;

    }

    //通过 >= <= < >  = 来分个算术表达式  并且如果表达式没有这个 只有一个 类似于！a  分割为3部分 返回指定部分
    public static List<String> spiltExpressionWithSingle(String expression){
        List<String> result = new ArrayList<>();
        // 正则表达式用于匹配 >=、<=、> 或 <
        String regex = "(\\s*)(>=|<=|>|<|==|!=)(\\s*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);

        if (matcher.find()) {
            // 使用正则表达式的捕获组获取分割后的三部分
            result.add(expression.substring(0, matcher.start()).trim());
            result.add(matcher.group(2).trim());
            result.add(expression.substring(matcher.end()).trim());

            //String firstPart = expression.substring(0, matcher.start()).trim();
//            String operator = matcher.group(2).trim();
//            String secondPart = expression.substring(matcher.end()).trim();

        }else {
            result.add(expression);
        }
        //System.out.println("spiltExpressionWithSingle: "+result);
        return result;

    }

    //通过 >= <= < >  = 来分个算术表达式 分割为3部分 返回指定部分
    public static String spiltExpressionByIndex(String expression,int index){
        List<String> result = new ArrayList<>();
        // 正则表达式用于匹配 >=、<=、> 或 <
        String regex = "(\\s*)(>=|<=|>|<|=)(\\s*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);

        if (matcher.find()) {
            // 使用正则表达式的捕获组获取分割后的三部分
            result.add(expression.substring(0, matcher.start()).trim());
            result.add(matcher.group(2).trim());
            result.add(expression.substring(matcher.end()).trim());

            //String firstPart = expression.substring(0, matcher.start()).trim();
//            String operator = matcher.group(2).trim();
//            String secondPart = expression.substring(matcher.end()).trim();

        }
       // System.out.println("spiltExpressionByIndex: "+result.get(index));
        return result.get(index);

    }


    public static String[] convertStringToArray(String input) {
        char[] charArray = input.toCharArray();
        String[] stringArray = new String[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            stringArray[i] = Character.toString(charArray[i]);
        }

        return stringArray;
    }

    //通过 >= <= < >  ==  != -= +=来分个算术表达式 分割为3部分  判断表达是分割
    public static List<String> spiltDeExpression(String expression){
        List<String> result = new ArrayList<>();

        // 正则表达式用于匹配 >=、<=、> 或 <
        String regex = "(\\s*)(>=|<=|>|<|==|!=|-=|\\+=)(\\s*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);

        if (matcher.find()) {
            // 使用正则表达式的捕获组获取分割后的三部分
            String firstPart = expression.substring(0, matcher.start()).trim();
            result.add(firstPart);
            String operator = matcher.group(2).trim();
            result.add(operator);
            String secondPart = expression.substring(matcher.end()).trim();
            result.add(secondPart);

           // System.out.println(result);

            return result;
        }

        return null;

    }


    //表达式结果判断
    public static  Boolean ExpressionEvaluator(String s,String value,boolean isBool,String name) throws ScriptException {
        if(s == null){
            return  null;
        }

        // 创建 JavaScript 引擎
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        try {
            if (!isBool){
                Integer A = Integer.valueOf(value);
                // 设置变量 A 到引擎上下文中
                engine.put(name, A);
                // 通过引擎求值表达式
                boolean result = (boolean) engine.eval(s);

                // 输出结果
              //  System.out.println("Expression: " + s);
              //  System.out.println("Evaluated to: " + result);

                return result;

            }else
            {
                Boolean A = Boolean.valueOf(value);

                // 设置变量 A 到引擎上下文中
                engine.put(name, A);
                // 通过引擎求值表达式
                boolean result = (boolean) engine.eval(s);

                // 输出结果
               // System.out.println("Expression: " + s);
               // System.out.println("Evaluated to: " + result);
                return result;
            }

        }catch (Exception e){
            System.err.println("Error evaluating expression: " + e.getMessage());
            return null;
        }


    }

    public static String removeEscape(String input){

        // 去除所有空格和换行字符
        String formattedString = input.replaceAll("\\s+", "");
        return formattedString;
    }


    public static String removeEscapeExOne(String input){

        // 去除所有空格和换行字符
        String formattedString = input.replaceAll("\\s+", " ");
        return formattedString;
    }

    public static String removeEscapeSaveStartEscape(String input){

        // 找到开头的空格并保存
        int startIndex = 0;
        while (startIndex < input.length() && Character.isWhitespace(input.charAt(startIndex))) {
            startIndex++;
        }

        // 去除开头空格后的所有空格和换行字符
        String formattedString = input.substring(0, startIndex) + input.substring(startIndex).replaceAll("\\s+", "");

        return formattedString;
    }

    public static String removelastEOF(String input){
        int index = input.lastIndexOf("<EOF>");

        if (index != -1) {
            String output = input.substring(0, index);
            return output;
        } else {
            return  input; // 如果字符串中没有"<EOF>"，则不做任何更改。
        }
    }

    public static  void WriteFile(String fileName, String context){
        try {
            // 指定要写入的文件路径
            String filePath = fileName;

            // 创建 FileWriter 对象
            FileWriter fileWriter = new FileWriter(filePath);


            // 创建 BufferedWriter 对象以提高写入性能
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // 写入字符串到文件
            bufferedWriter.write(context);

            // 关闭 BufferedWriter 和 FileWriter
            bufferedWriter.close();
            fileWriter.close();



            } catch (IOException e) {
                e.printStackTrace();
            }
    }


    public static List<String> spiltAssignmentText(String input){
        List<String> results = new ArrayList<>();

        // Define a regular expression pattern to match variable declarations
        Pattern pattern = Pattern.compile("(\\w+(?:\\s+\\w+)*)\\s+(\\w+)\\s*(?:=\\s*(.*?))?;");

        // Create a Matcher to find matches in the input string
        Matcher matcher = pattern.matcher(input);

        // Iterate through matches and extract type, name, and initial value
        while (matcher.find()) {
            String s1 = matcher.group(1);
            if (s1.equals("bool")||s1.contains("int")){
                results.add(matcher.group(1));
                results.add(matcher.group(2));

                String initialValue = null;
                // If initialValue is null, set it to a default value
                if (matcher.group(3) == null) {
                    if (matcher.group(1).equals("bool")){
                        initialValue = "false";
                    }else if (matcher.group(1).contains("int")){
                        initialValue = "0";
                    }
                }else {
                    initialValue = matcher.group(3);
                }
                results.add(initialValue);


            }


        }
        return results;


    }

    public static ArrayList<Long> findNumbers(String str) {
        ArrayList<Long> numbers = new ArrayList<>();
        System.out.println("findNumbers"+str);
        Pattern pattern = Pattern.compile("\\d+"); // 正则表达式，匹配任何数字序列
        Matcher matcher = pattern.matcher(str);

        while (matcher.find()) {
            try {
                numbers.add(Long.parseLong(matcher.group()));
            } catch (NumberFormatException e) {
                System.out.println("error: " + matcher.group());
            }
        }

        return numbers;
    }

    public static  void javaEngine(String name,Integer value,String s){




//        // 遍历表达式列表
//        for (String expression : expressions) {
//            try {
//                // 通过引擎求值表达式
//                boolean result = (boolean) engine.eval(expression);
//
//                // 输出结果
//                System.out.println("Expression: " + expression);
//                System.out.println("Evaluated to: " + result);
//            } catch (ScriptException e) {
//                e.printStackTrace();
//            }
//        }
    }

}
