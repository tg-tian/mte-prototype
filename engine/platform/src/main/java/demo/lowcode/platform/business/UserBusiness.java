package demo.lowcode.platform.business;

import demo.lowcode.platform.dto.UserLogin;
import demo.lowcode.platform.entity.User;
import demo.lowcode.platform.mapper.UserMapper;
import demo.lowcode.platform.util.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;

@Service
@RequiredArgsConstructor  //(自动生成构造方法,支持依赖注入)
public class UserBusiness {
    //private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    //todo:用于实现用户注册
    public void registerUser(){}

    /**
     * 用户登录
     * @param userLogin
     * @return
     */
    public String login(UserLogin userLogin){
        User user = userMapper.login(userLogin);
        if(user == null){
            return null;
        }else {
            return TokenUtils.createToken(user);
        }
    }

    public User getUser(UserLogin userLogin){
        return userMapper.login(userLogin);
    }


    /**用于检查密码
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public String encodePassword(String rawPassword){
        return passwordEncoder.encode(rawPassword);
    }*/
}
