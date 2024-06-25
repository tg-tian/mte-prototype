package demo.lowcode.engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RTProcess {
    private String processId;
    private Integer processStatus;//1:未开始;2:进行中;3:挂起;4:终止
    private Map<String, Integer> actionStatusList;//1：未开始；2：进行中；3：已完成；4：中止/父节点中止
    private Date startTime;
}
