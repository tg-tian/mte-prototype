package com.ubml.devicemodel.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity {
    private String name;
    private String description;
    private String type;
    private String command;
    private String signature;
}
