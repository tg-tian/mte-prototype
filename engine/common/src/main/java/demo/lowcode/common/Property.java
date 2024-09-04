package demo.lowcode.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Property {
    private String code;
    private String name;
    private String type;
}
