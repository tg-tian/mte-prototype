package demo.lowcode.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Param {
    private String code;
    private String name;
    private String type;
    private List<Object> optional;

    public Param(String code, String name, String type) {
        this.code = code;
        this.name = name;
        this.type = type;
    }
}
