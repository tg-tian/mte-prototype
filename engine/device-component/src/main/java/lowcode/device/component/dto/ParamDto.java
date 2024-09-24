package lowcode.device.component.dto;

import demo.lowcode.common.Param;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamDto {
    public List<Param> inputParams;
    public String outputParam;
}
