package com.ubml.devicemodel;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inspur.edp.lcm.metadata.api.AbstractMetadataContent;
import com.inspur.edp.lcm.metadata.api.IMetadataContent;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class DeviceModelEntity extends AbstractMetadataContent implements IMetadataContent {
    protected DeviceService deviceService = null;

    public List<DeviceModelEntityProperty> properties = new ArrayList<>();

    public List<String> operations = new ArrayList<>();

    public List<String> events;

    private String code;

    private String name;

    public void bindService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    public void addProperties(DeviceModelEntityProperty property) {
        properties.add(property);
    }

    public void addOperation(String operation) {
        operations.add(operation);
    }

    public void addEvent(String event) {
        events.add(event);
    }

    public int invokeOperation(String operation, Object... args) {
        return 0;
    }
    public void addEventListener(String eventName, EventListener eventListener){}
}
