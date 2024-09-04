package demo.lowcode.common;

import demo.lowcode.common.Param;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Command {
    private String commandName;
    private List<Param> inputParam;
    private String outputParam;
}
