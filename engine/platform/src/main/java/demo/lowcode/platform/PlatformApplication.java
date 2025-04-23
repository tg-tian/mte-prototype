package demo.lowcode.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = {"demo.lowcode", "lowcode.device"})
@MapperScan("demo.lowcode.platform.mapper")
public class PlatformApplication {

    public static void main(String[] args) {
        // 设置JVM默认时区为北京时间（东八区）
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(PlatformApplication.class, args);
    }

}
