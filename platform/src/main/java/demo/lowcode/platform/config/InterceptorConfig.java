package demo.lowcode.platform.config;

import demo.lowcode.platform.common.CommonConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        // 图片访问
        registry.addResourceHandler("/images/**").addResourceLocations("file:"+ CommonConfig.getWorkspacePath()+"/");
    }
}
