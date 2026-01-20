package demo.lowcode.platform.business;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import demo.lowcode.platform.dto.DeviceMapperResult;
import demo.lowcode.platform.entity.DeviceLibrary;
import demo.lowcode.platform.mapper.DeviceLibraryMapper;
import org.springframework.stereotype.Service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
}
