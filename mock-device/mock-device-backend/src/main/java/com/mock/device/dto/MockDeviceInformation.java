package com.mock.device.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MockDeviceInformation {
    private String status;
    private String currentOperation;
}
