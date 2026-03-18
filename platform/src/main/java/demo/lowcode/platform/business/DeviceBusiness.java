package demo.lowcode.platform.business;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import demo.lowcode.platform.dto.DeviceMapperResult;
import demo.lowcode.platform.entity.Device;
import demo.lowcode.platform.mapper.DeviceMapper;
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
public class DeviceBusiness extends ServiceImpl<DeviceMapper, Device> {

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
        ClassPathResource resource = new ClassPathResource("devicemapper/" + fileName);

        if (!resource.exists()) {
            throw new RuntimeException("Mapper文件不存在: " + fileName);
        }

        try (InputStream inputStream = resource.getInputStream()) {
            String content = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            return new DeviceMapperResult(content, device.getModelId());
        } catch (IOException e) {
            throw new RuntimeException("读取Mapper文件失败", e);
        }
    }

    public List<Device> listAll() {
        return this.list();
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
                    .map(e -> "    \"" + e.getKey() + "\": \"" + e.getValue() + "\"")
                    .collect(Collectors.joining(",\n")) + "\n  }";
        }

        String actionMapStr = "{}";
        if (entity.getActionMap() != null) {
            actionMapStr = "{\n" + entity.getActionMap().entrySet().stream()
                    .map(e -> "    \"" + e.getKey() + "\": `" + e.getValue().replace("`", "\\`") + "`")
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
                  actionMap: Record<string, string> = %s;

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

                  async callAction(actionId: string, params: any): Promise<any> {
                    const impl = this.actionMap[actionId];
                    if (impl) {
                      try {
                        // 创建一个函数并执行
                        const fn = new Function('params', 'device', impl);
                        return await fn(params, this);
                      } catch (e) {
                        console.error(`Execute action ${actionId} failed:`, e);
                        throw e;
                      }
                    }
                    return null;
                  }

                  match(rawDevice: any): boolean {
                    return true;
                  }
                }
                """.formatted(className, entity.getDeviceId(), entity.getProvider(), propertyMapStr, actionMapStr);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("生成Mapper文件失败: " + e.getMessage());
        }
    }
}

