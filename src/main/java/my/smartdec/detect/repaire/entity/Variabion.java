package my.smartdec.detect.repaire.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Variabion {
    //互斥变量的类型 为 int uint256
    String type;
    //互斥变量的变量名
    String name;
    //互斥变量的初始值
    String value;
    //互斥所语句
    String  mutexExpression;

    //判断其是否具有 结果为true的断言语句
    boolean isDetration;
    //判断其是否改变过
    boolean isChange;
    //判断其是否恢复
    boolean isRecovery;

    public Variabion(String type,String name,String value){
        this.type = type;
        this.name = name;
        if (value == null){
            if (type.equals("bool")){
                this.value = "false";
            }else if (type.contains("int")){
                this.value = "0";
            }
        }else {
            this.value = value;
        }

        mutexExpression = null;
    }

    public void setIsRecovery(boolean isRecovery){
        this.isRecovery = isRecovery;
    }
}
