package demo.lowcode.platform.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data //@Data   ：注解在类上；提供类所有属性的 getting 和 setting 方法，此外还提供了equals、canEqual、hashCode、toString 方法
@Component
public class DomainMeta {
    private String domainId;
    private String domainName;
    private Map<String, List<String>> componentType;
}
