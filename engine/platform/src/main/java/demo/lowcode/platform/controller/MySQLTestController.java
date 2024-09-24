package demo.lowcode.platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;



@RestController
/**
 * 这个注解表示该类是一个 Spring MVC 的控制器，能够处理 HTTP 请求并返回结果。使用 @RestController 表示该控制器的方法默认会返回数据（通常是 JSON 或纯文本），
 * 而不是视图（像传统的 @Controller）。
 * */
public class MySQLTestController {

    @Autowired
    /*通过 @Autowired 注解，Spring 会自动注入一个 JdbcTemplate 实例，这意味着开发者不需要手动创建 JdbcTemplate 对象，Spring 会自动管理其生命周期。*/
    private JdbcTemplate jdbcTemplate;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @GetMapping("/test_db")
    public void testDB(){
        try{
            jdbcTemplate.execute("INSERT INTO domain VALUES ('002','SmartBuilding','This is a SmartBuilding');");
            System.out.println("connect successful!");
        }catch (Exception ERROR){
            System.out.println("connect error");
        }
    }

    //检测自动解码是否可行
    @GetMapping("/get_password")
    public void getPassword(){
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("G0CvDz7oJn6");
        String derectpassword = encryptor.decrypt("eeV5YrectNQ8xRw4KUgI2bC5lXhZNevW");
        System.out.println(derectpassword+"//////////"+dbPassword);
    }
}
