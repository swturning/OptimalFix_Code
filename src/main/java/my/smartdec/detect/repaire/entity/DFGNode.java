package my.smartdec.detect.repaire.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DFGNode {
    private String name;
    private String operation;  // 可以是变量名、操作符等
    private String value;      //相关的操作语句 或者添加的value
    private boolean isInput;  // 是否是输入节点
    private boolean isOutput;  // 是否是输出节点

    public DFGNode(String name, String operation, boolean isInput, boolean isOutput) {
        this.name = name;
        this.operation = operation;
        this.isInput = isInput;
        this.isOutput = isOutput;
    }

    public String getName() {
        return name;
    }

    public String getOperation() {
        return operation;
    }

    public boolean isInput() {
        return isInput;
    }

    public boolean isOutput() {
        return isOutput;
    }

    @Override
    public String toString() {
        return name + " (" + operation + ")";
    }
}



