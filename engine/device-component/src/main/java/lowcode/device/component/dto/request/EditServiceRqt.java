package lowcode.device.component.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditServiceRqt {
    public String deviceType;
    public String serviceName;
}
