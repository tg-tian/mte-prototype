package demo.lowcode.platform.dto;

import demo.lowcode.platform.model.device.ProtocolConfig;
import lombok.Data;

@Data
public class NewDevice {
    private String code;
    private String name;
    private Long deviceTypeId;
    private Long sceneId;
    private String protocolType;
    private ProtocolConfig protocolConfig;
    private String field;
}
