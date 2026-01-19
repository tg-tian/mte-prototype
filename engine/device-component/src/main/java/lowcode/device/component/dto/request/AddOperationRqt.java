package lowcode.device.component.dto.request;

import demo.lowcode.common.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOperationRqt {
    public String deviceType;
    public Command command;
}
