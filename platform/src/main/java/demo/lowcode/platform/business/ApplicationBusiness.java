package demo.lowcode.platform.business;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ApplicationBusiness {
    @Resource
    private ProcessBusiness processBusiness;

    // TODO: load application definition
    // TODO: 解释引擎：可能要加载多个app，不同的app应该有不同的入口
    public void loadApplication(String appPath) {
        // 读取该应用的json文件

        // 新增应用，获取应用信息

        // 加载该应用流程
    }
}
