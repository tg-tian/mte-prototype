package demo.lowcode.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExecuteActionArgs {
    private String actionId;
    private Map<String, Object> args;
}
