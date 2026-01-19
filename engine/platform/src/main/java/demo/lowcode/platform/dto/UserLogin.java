package demo.lowcode.platform.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserLogin {
    private String username; //这里是不是应该写成userName
    private String password;
}
