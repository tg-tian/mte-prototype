package demo.lowcode.platform.model.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private String identify;//事件标识符
    private String name;//事件名称
    private String type;//事件类型：信息info、告警warning、故障error
    private List<Property> outputData;
}
