package lowcode.device.generator.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import lowcode.device.generator.GeneratorConfig;
import lowcode.device.generator.core.DeviceGenerator;
import lowcode.device.generator.core.ServiceGenerator;
import lowcode.device.generator.dto.ProgressData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@CrossOrigin
@ApiModel(value = "设备代码生成与打包")
public class DeviceGenerateController {

    @ApiOperation(value = "生成java代码并打包发布")
    @GetMapping(value = "/device/publish")
    public SseEmitter publishDevice(@RequestParam String deviceType) {
        // SseEmitter 是 Spring 提供的一个用于支持服务器推送事件（Server-Sent Events, SSE）的类。它允许服务器向客户端持续推送数据
        // SseEmitter 在服务器端打开一个长连接，客户端通过 EventSource 接受服务器推送的数据。服务器可以在一段时间内多次向客户端发送事件，而无需关闭连接
        SseEmitter emitter = new SseEmitter();
        DeviceGenerator generator = new DeviceGenerator(deviceType, GeneratorConfig.getDefinitionPath() +deviceType+"/");

        new Thread(() -> {
            try {
                // 模拟设备生成过程
                emitter.send(new ProgressData("开始生成代码...", 25));
                generator.generate();
                Thread.sleep(1000);  // 模拟一些延时操作

                emitter.send(new ProgressData("代码生成成功， 开始打包...", 50));
                generator.buildAndPackage();
                Thread.sleep(1000);

                emitter.send(new ProgressData("打包成功， 开始拷贝jar包到工作目录", 75));
                generator.copyJarFile();
                Thread.sleep(1000);

                emitter.send(new ProgressData("发布成功", 100));
                emitter.complete();  // 完成后关闭连接
            } catch (Exception e) {
                try {
                    emitter.send("打包失败: " + e.getMessage());
                } catch (Exception ioException) {
                    ioException.printStackTrace();
                }
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }
}
