package demo.lowcode.platform.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 *领域设备绑定联合主键类
 */
public class DeviceBindingId implements Serializable {
    private long deviceTypeId;
    private long domainId;

    // 重写 equals() 和 hashCode() 方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceBindingId that = (DeviceBindingId) o;
        return deviceTypeId == that.deviceTypeId && domainId == that.domainId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceTypeId, domainId);
    }

}
