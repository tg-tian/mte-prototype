package demo.lowcode.platform.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * 设备注册表联合主键类
 */
public class DeviceRegisterId implements Serializable {
    private long deviceId;
    private long sceneId;

    // 重写 equals() 和 hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceRegisterId that = (DeviceRegisterId) o;
        return deviceId == that.deviceId && sceneId == that.sceneId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId, sceneId);
    }
}
