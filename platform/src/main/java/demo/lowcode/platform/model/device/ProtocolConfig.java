package demo.lowcode.platform.model.device;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolConfig implements Serializable {
    private String type;//物联网平台类型：aliyun/inspur/none
    private Map<String, String> configs;
}
