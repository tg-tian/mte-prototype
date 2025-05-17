package demo.lowcode.platform.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootConfiguration
public class WebConfigurer implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);

        registry.addMapping("/**")
//                .allowedOrigins("*")
                .allowedOriginPatterns("*")
                .exposedHeaders("Content-Disposition")
                .allowedMethods("POST", "PUT", "OPTIONS", "DELETE", "GET")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
