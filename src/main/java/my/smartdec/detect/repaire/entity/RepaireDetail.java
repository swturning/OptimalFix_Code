package my.smartdec.detect.repaire.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Comparator;

@Data
@AllArgsConstructor
public class RepaireDetail  implements Comparable<RepaireDetail> {
    //分为 update add delete  virtual 4种
    public String type;
    //表明需要修改的行数
    public Integer line;

    //表示添加多少行
    public Integer addLine = 1;
    //
    public String oriContext;

    public String newContext;

    public RepaireDetail(String type,Integer line,String oriContext,String newContext){
        this.type = type;
        this.line = line;
        this.oriContext = oriContext;
        this.newContext = newContext;
    }

    public RepaireDetail(String type,Integer line,String oriContext,String newContext,Integer addline){
        this.type = type;
        this.line = line;
        this.oriContext = oriContext;
        this.newContext = newContext;
        this.addLine = addline;
    }


    @Override
    public int compareTo(RepaireDetail other) {
        return Integer.compare(this.line, other.line);
    }
}
