package demo.lowcode.platform.model.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataType {
    private String type;
    private Map<String, String> specs;//json格式，number类型包括min\max\step属性，bool类型包括0\1，string类型包括length
}
