package com.mock.device;

import com.mock.device.model.Device;
import com.mock.device.model.DeviceA;
import com.mock.device.model.DeviceB;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class DeviceManager {
    private static DeviceA deviceA = new DeviceA();

    private static DeviceB deviceB = new DeviceB();

    public static DeviceA getDeviceA() {
        return deviceA;
    }

    public static DeviceB getDeviceB() {
        return deviceB;
    }
}
