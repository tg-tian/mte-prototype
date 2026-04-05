package demo.lowcode.platform.business.mappergenerator;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeviceMapperGeneratorFactory {
    private final List<DeviceMapperGenerator> generators;

    public DeviceMapperGeneratorFactory(List<DeviceMapperGenerator> generators) {
        this.generators = generators;
    }

    public DeviceMapperGenerator getGenerator(String provider) {
        return generators.stream()
                .filter(generator -> generator.supports(provider))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("暂不支持厂商 " + provider + " 的 Mapper 生成"));
    }
}
