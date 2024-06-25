package demo.lowcode.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActionExecResult {
    private int code;
    private Map<String, Object> outputParams;
}
