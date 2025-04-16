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
public class Service {
    private String identify;//服务标识符
    private String name;//服务名称
    private List<Property> inputData;
    private List<Property> outputData;
}
