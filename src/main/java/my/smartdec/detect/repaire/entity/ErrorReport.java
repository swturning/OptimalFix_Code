package my.smartdec.detect.repaire.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

//用于记录整个漏洞
@Data
@AllArgsConstructor
public class ErrorReport {

    //漏洞模式
    String pattern;

    //表示其实第几个同类型的错误
    Integer index;

    //风险等级
    int level;

    //
    int startLine;

    int stopLine;

    //判断是否为假阳性 也就是 二次过滤
    boolean isDectiontwic = false;

    //具有漏洞的代码内容
    String context;

    //可以选择的修复模式 如果为重入漏洞 A 表示给位transfer B 表示更改为 互斥锁形式
    String repaireType;

    //预定需要的修复模式
    //如果为重入漏洞   A 表示给位transfer B 表示更改为 互斥锁形式
    String chooseType;

    //这表示 有用户输入的想换状态变量名 修饰函数名 和 函数名
    List<String> varNotes = new ArrayList<>();

    //表示当前漏洞节点
    Node errorNode;

    //用于修复的全节点
    Node sourceNode;

    public ErrorReport(Integer index,String pattern, int level, int startLine, int stopLine, String context,  Node errorNode) {
        this.index = index;
        this.pattern = pattern;
        this.level = level;
        this.startLine = startLine;
        this.stopLine = stopLine;
        this.context = context;
        this.errorNode = errorNode;
        if (pattern.equals("ree001")||pattern.equals("ree003")||pattern.equals("ree002")){
            chooseType = "B";
        }else if (pattern.equals("sus001")){
            chooseType = "B";
        }
        this.sourceNode = null;
    }


}
