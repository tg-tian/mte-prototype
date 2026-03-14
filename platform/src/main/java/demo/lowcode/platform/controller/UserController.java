package demo.lowcode.platform.controller;

import demo.lowcode.platform.business.UserBusiness;
import demo.lowcode.platform.dto.UserLogin;
import demo.lowcode.platform.entity.User;
import io.swagger.annotations.Api;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/auth" )
@Api(value = "用户接口",tags={"用户管理"})
public class UserController {

    @Resource
    UserBusiness userBusiness;

    @PostMapping(value = "/login")
    public ResponseEntity <?> login(@RequestBody UserLogin userLogin)  //@RequestBody 只能绑定 一个对象，不能直接映射到多个参数。
    {
        try {
            String token = userBusiness.login(userLogin);
            User user = userBusiness.getUser(userLogin);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", token);
            responseData.put("user", user);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }catch (RuntimeException e)
        {
            System.err.println("登录失败");
            return new ResponseEntity<>("用户登录失败", HttpStatus.CONFLICT);
        }
    }

}
