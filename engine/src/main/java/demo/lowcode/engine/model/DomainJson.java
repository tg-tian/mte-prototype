package demo.lowcode.engine.model;

import lombok.Data; //lombok能够达到的效果就是在源码中不需要写一些通用的方法，但是在编译生成的字节码文件中会帮我们生成这些方法，
import org.springframework.stereotype.Component; //导入Spring注解包，Component是Spring的一个注解，表示这个类是一个组件

import java.util.List;
import java.util.Map;

@Data
@Component
public class DomainJson {
    private String domainID;
    private  String domainName;
    private Map<String, List<String>> domainField;
    //private List<Map<String,List<String>>> componentTypeList;
}

