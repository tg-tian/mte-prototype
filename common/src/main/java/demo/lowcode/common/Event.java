package demo.lowcode.common;

import lombok.Data;

@Data
public abstract class Event {
    protected String message;
    protected int code;
}
