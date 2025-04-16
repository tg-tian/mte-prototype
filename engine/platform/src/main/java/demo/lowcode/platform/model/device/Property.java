package demo.lowcode.platform.model.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Property {
    private String identify;//属性标识符
    private String name;//属性名称
    private String accessMode;//属性读写类型
    private DataType dataType;//属性数据类型
}
