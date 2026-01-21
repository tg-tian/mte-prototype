package demo.lowcode.platform.business;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import demo.lowcode.platform.dto.DeviceMapperResult;
import demo.lowcode.platform.entity.DeviceLibrary;
import demo.lowcode.platform.mapper.DeviceLibraryMapper;
import org.springframework.stereotype.Service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceLibraryBusiness extends ServiceImpl<DeviceLibraryMapper, DeviceLibrary> {

    public DeviceMapperResult getMapperContent(String provider, String deviceModel) {
        QueryWrapper<DeviceLibrary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("provider", provider)
                    .eq("device_model", deviceModel);
        
        DeviceLibrary deviceLibrary = this.getOne(queryWrapper);
        if (deviceLibrary == null) {
            throw new RuntimeException("未找到对应的设备库信息");
        }

        String mapperPath = deviceLibrary.getDeviceMapperPath();
        if (mapperPath == null || mapperPath.isEmpty()) {
            throw new RuntimeException("设备Mapper路径为空");
        }
        String fileName = mapperPath;
        ClassPathResource resource = new ClassPathResource("devicemapper/" + fileName);

        if (!resource.exists()) {
            throw new RuntimeException("Mapper文件不存在: " + fileName);
        }

        try (InputStream inputStream = resource.getInputStream()) {
            String content = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            return new DeviceMapperResult(content, deviceLibrary.getDeviceTypeName());
        } catch (IOException e) {
            throw new RuntimeException("读取Mapper文件失败", e);
        }
    }

    public List<DeviceLibrary> listAll() {
        return this.list();
    }

    @Override
    public boolean save(DeviceLibrary entity) {
        processMapperFile(entity);
        return super.save(entity);
    }

    @Override
    public boolean updateById(DeviceLibrary entity) {
        processMapperFile(entity);
        return super.updateById(entity);
    }

    private void processMapperFile(DeviceLibrary entity) {
        if (entity.getDeviceMapperPath() == null || entity.getDeviceMapperPath().isEmpty()) {
            String fileName = generateFileName(entity.getProvider(), entity.getDeviceModel());
            entity.setDeviceMapperPath(fileName);
        }

        generatePhysicalFile(entity);
    }

    private String generateFileName(String provider, String model) {
        String sanitizedModel = model.replaceAll("[^a-zA-Z0-9]", "");
        String capitalizedProvider = provider.substring(0, 1).toUpperCase() + provider.substring(1);
        return capitalizedProvider + sanitizedModel + "Mapper.ts";
    }

    private void generatePhysicalFile(DeviceLibrary entity) {
        String fileName = entity.getDeviceMapperPath();
        String projectPath = System.getProperty("user.dir");
        String resourcesPath = "/src/main/resources/devicemapper/";
        File directory = new File(projectPath + resourcesPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, fileName);
        if (file.exists()) {
            // return;
        }

        String className = fileName.replace(".ts", "");
        String propertyMapStr = "{}";
        if (entity.getPropertyMap() != null) {
            propertyMapStr = "{\n" + entity.getPropertyMap().entrySet().stream()
                    .map(e -> "    " + e.getKey() + ": '" + e.getValue() + "'")
                    .collect(Collectors.joining(",\n")) + "\n  }";
        }

        String content = """
                import mqtt from 'mqtt';
                import { DeviceMapper} from '../device-mapper';
                import { ProviderConfig } from '../../domain/provider-config';
                import { BaseDeviceModel } from '../../domain/model';

                export class %s implements DeviceMapper {
                  metaModel : BaseDeviceModel;
                  deviceModel = '%s';
                  provider = '%s';
                  private client: mqtt.MqttClient;
                  private cfg: ProviderConfig;
                  propertyMap: Record<string, string> = %s;

                  constructor(config: ProviderConfig , metaModel: BaseDeviceModel) {
                    this.cfg = config;
                    this.metaModel = metaModel;
                    this.client = mqtt.connect(this.cfg.communication.baseUrl);
                  }

                  mapProperties(rawProps: any): Record<string, any> {
                    const mapped: Record<string, any> = {};
                    for (const [key, value] of Object.entries(rawProps)) {
                      const target = this.propertyMap[key as keyof typeof this.propertyMap];
                      if (target) {
                        mapped[target] = value;
                      } else {
                        mapped[key] = value;
                      }
                    }
                    return mapped;
                  }

                  mapEvent(rawEvent: any): any | null {
                    return null;
                  }
                }
                """.formatted(className, entity.getDeviceModel(), entity.getProvider(), propertyMapStr);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("生成Mapper文件失败: " + e.getMessage());
        }
    }
}
