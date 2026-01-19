package com.mock.device.business;

import com.mock.device.DeviceManager;
import com.mock.device.dto.MockDeviceInformation;
import com.mock.device.model.DeviceA;
import com.mock.device.model.DeviceB;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class MockBusiness {

    public MockDeviceInformation getAMockData(){
        DeviceA deviceA = DeviceManager.getDeviceA();
        return new MockDeviceInformation(deviceA.getCurrentStatus(), deviceA.getCurrentOperation());
    }

    public MockDeviceInformation getBMockData(){
        DeviceB deviceB = DeviceManager.getDeviceB();
        return new MockDeviceInformation(deviceB.getCurrentStatus(), deviceB.getCurrentOperation());
    }

    public void executeOperationA(String operation, Map<String, String> params){
        DeviceA deviceA = DeviceManager.getDeviceA();
        if (Objects.equals(operation, "start")){
            deviceA.start();
        }else if (Objects.equals(operation, "makeCoffee")){
            deviceA.makeCoffee(params.get("coffeeType"));
        }else {
            throw new RuntimeException("咖啡机器人A不支持操作"+operation);
        }
    }

    public void executeOperationB(String operation, Map<String, String> params){
        DeviceB deviceB = DeviceManager.getDeviceB();
        if (Objects.equals(operation, "start")){
            deviceB.start();
        }else if (Objects.equals(operation, "on")){
            deviceB.on(params.get("coffeeType"));
        }else if (Objects.equals(operation, "check")){
            deviceB.check();
        }else {
            throw new RuntimeException("咖啡机器人A不支持操作"+operation);
        }
    }
}
