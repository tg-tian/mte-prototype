package demo.lowcode.platform.dto;

import demo.lowcode.platform.entity.DeviceType;
import demo.lowcode.platform.model.device.ProtocolConfig;
import lombok.Data;

import java.util.List;

@Data
public class NewDevice {
    private Long id;
    private String deviceCode;
    private String deviceName;
    private DeviceType deviceType;
    private String code;
    private String name;
    private Long deviceTypeId;
    private Long sceneId;
    private String protocolType;
    private ProtocolConfig protocolConfig;
    private String deviceLocation;
    private String devicePosition;
    private List<Connection> connections;
    private boolean intelligent;

    @Data
    public static class Connection {
        Long id;
        String deviceName;
        String position;
    }
}
