package my.smartdec.detect.repaire.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

    //选择包含目标参数的 字符串
    public static List<String> ListContaines(List<String> list,String name){
        List<String> newString = new ArrayList<>();
        for (String s : list){

            if (s.contains(name)){
                newString.add(s);
            }
        }
        return newString;
    }
}
