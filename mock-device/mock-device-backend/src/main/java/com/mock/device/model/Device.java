package com.mock.device.model;

import lombok.Data;

@Data
public abstract class Device {
    private String currentStatus = "空闲"; // 空闲/忙碌/错误
    private String currentOperation = "";
}
