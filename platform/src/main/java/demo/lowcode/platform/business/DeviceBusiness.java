package demo.lowcode.platform.business;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import demo.lowcode.platform.business.mappergenerator.DeviceMapperGenerator;
import demo.lowcode.platform.business.mappergenerator.DeviceMapperGeneratorFactory;
import demo.lowcode.platform.dto.DeviceMapperResult;
import demo.lowcode.platform.entity.Device;
import demo.lowcode.platform.mapper.DeviceMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

@Service
public class DeviceBusiness extends ServiceImpl<DeviceMapper, Device> {
    @Resource
    private DeviceMapperGeneratorFactory deviceMapperGeneratorFactory;

    public DeviceMapperResult getMapperContent(String provider, String deviceId) {
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("provider", provider)
                .eq("device_id", deviceId);

        Device device = this.getOne(queryWrapper);
        if (device == null) {
            throw new RuntimeException("未找到对应的设备信息");
        }

        String mapperPath = device.getDeviceMapperPath();
        if (mapperPath == null || mapperPath.isEmpty()) {
            throw new RuntimeException("设备Mapper路径为空");
        }
        String fileName = mapperPath;
        File mapperFile = resolveMapperFile(fileName);

        try {
            if (mapperFile.exists()) {
                String content = Files.readString(mapperFile.toPath(), StandardCharsets.UTF_8);
                return new DeviceMapperResult(content, device.getModelId());
            }

            ClassPathResource resource = new ClassPathResource("devicemapper/" + fileName);
            if (!resource.exists()) {
                throw new RuntimeException("Mapper文件不存在: " + fileName);
            }

            try (InputStream inputStream = resource.getInputStream()) {
                String content = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
                return new DeviceMapperResult(content, device.getModelId());
            }
        } catch (IOException e) {
            throw new RuntimeException("读取Mapper文件失败", e);
        }
    }

    public List<Device> listAll() {
        return this.list();
    }

    public void generateMapper(Long id) {
        Device device = this.getById(id);
        if (device == null) {
            throw new RuntimeException("设备不存在");
        }
        processMapperFile(device);
        this.updateById(device);
    }

    @Override
    public boolean save(Device entity) {
        processMapperFile(entity);
        return super.save(entity);
    }

    @Override
    public boolean updateById(Device entity) {
        processMapperFile(entity);
        return super.updateById(entity);
    }

    private void processMapperFile(Device entity) {
        if (entity.getDeviceMapperPath() == null || entity.getDeviceMapperPath().isEmpty()) {
            String fileName = generateFileName(entity.getProvider(), entity.getDeviceId());
            entity.setDeviceMapperPath(fileName);
        }

        generatePhysicalFile(entity);
    }

    private String generateFileName(String provider, String model) {
        String sanitizedModel = model.replaceAll("[^a-zA-Z0-9]", "");
        String capitalizedProvider = provider.substring(0, 1).toUpperCase() + provider.substring(1);
        return capitalizedProvider + sanitizedModel + "Mapper.ts";
    }

    private void generatePhysicalFile(Device entity) {
        String fileName = entity.getDeviceMapperPath();
        DeviceMapperGenerator generator = deviceMapperGeneratorFactory.getGenerator(entity.getProvider());
        File file = resolveMapperFile(fileName);
        File directory = file.getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            throw new RuntimeException("创建设备Mapper目录失败: " + directory.getAbsolutePath());
        }

        String className = fileName.replace(".ts", "");
        String content = generator.generateContent(entity, className);

        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("生成Mapper文件失败: " + e.getMessage());
        }
    }

    private File resolveMapperFile(String fileName) {
        String projectPath = System.getProperty("user.dir");
        File directory = new File(projectPath, "src/main/resources/devicemapper");
        return new File(directory, fileName);
    }
}

