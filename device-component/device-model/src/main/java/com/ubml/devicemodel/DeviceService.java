package com.ubml.devicemodel;

import com.ubml.devicemodel.common.CommandEntity;
import com.ubml.devicemodel.common.ParameterEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceService {
    private String code;

    private String name;

    private String device;

    private List<CommandEntity> commands;

    private List<ParameterEntity> parameters;
}
