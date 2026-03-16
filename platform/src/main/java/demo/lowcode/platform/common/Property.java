package demo.lowcode.platform.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Property {
    private String id;
    private String name;
    private String type;
    private String value;
    private String description;

    public Property(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
}

