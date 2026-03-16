package demo.lowcode.platform.common;

import lombok.Data;

@Data
public class ActionExecResult {
    private int code;
    private String message;
    private Object data;
}

