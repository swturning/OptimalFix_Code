package my.smartdec.detect.repaire.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Data
@AllArgsConstructor
//修复结果类
public class RepaireResult {
    public String fileName;

    public List<RepaireDetail> repaireDetailList = new ArrayList<>();

}
