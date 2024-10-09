package lowcode.device.component.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lowcode.device.component.entity.BrandService;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitServiceRqt {
    public String deviceType;
    public BrandService brandService;
}
